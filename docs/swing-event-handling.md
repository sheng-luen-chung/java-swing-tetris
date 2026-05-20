# Swing Event Handling 說明

Version 3 的事件流程分成幾個角色：`Timer` 推動時間，`InputHandler` 接收鍵盤，`GameEngine` 更新規則狀態，`AudioManager` 管理音訊，`GamePanel` 負責畫面。

## Timer：固定時間推進遊戲

`GamePanel` 建立 `javax.swing.Timer`：

```java
timer = new Timer(engine.getDropDelay(), event -> {
    engine.tick();
    ((Timer) event.getSource()).setDelay(engine.getDropDelay());
    repaint();
});
```

每次 Timer 觸發時：

1. 呼叫 `engine.tick()`。
2. 如果狀態是 `RUNNING`，目前方塊往下移動；如果不能下移，就固定、消行、計分並產生下一個方塊。
3. 呼叫 `engine.getDropDelay()` 更新 Timer delay，讓等級越高速度越快。
4. 呼叫 `repaint()` 要求 Swing 重畫畫面。

## Swing 鍵盤處理的一般作法

Swing 常見的鍵盤控制有兩種做法：`KeyListener` 與 Key Bindings。

## KeyListener

`KeyListener` 是比較直覺、適合入門教學的方式。做法是讓某個 component，例如 `JPanel`，註冊一個 listener：

```java
addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent event) {
        // handle key
    }
});
```

優點是容易理解：按鍵事件進來，就用 `switch` 判斷是哪個 key。

缺點是依賴 focus。只有當這個 component 真的拿到鍵盤焦點時，事件才會進來。所以通常要搭配：

```java
setFocusable(true);
requestFocusInWindow();
```

## Key Bindings

Swing 較正式、較推薦的做法是使用 `InputMap` 與 `ActionMap`。概念是先把「按鍵」綁到「動作名稱」，再把「動作名稱」綁到一個 `Action`。

範例：

```java
getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
    KeyStroke.getKeyStroke("LEFT"),
    "moveLeft"
);

getActionMap().put("moveLeft", new AbstractAction() {
    @Override
    public void actionPerformed(ActionEvent event) {
        engine.moveLeft();
        repaint();
    }
});
```

Key Bindings 的優點是比較不容易被 focus 問題卡住，也比較適合大型 Swing UI。缺點是對初學者稍微抽象一點。

## 本專案目前的鍵盤實作

這個專案目前使用 `KeyListener + InputHandler`。它保留 `KeyListener` 的直覺性，但把鍵盤判斷集中到 `InputHandler`，避免把一大段 `switch` 塞在 `GamePanel` 裡。

`GamePanel` 負責註冊：

```java
setFocusable(true);

addKeyListener(new InputHandler(engine, () -> {
    timer.setDelay(engine.getDropDelay());
    repaint();
}));
```

`InputHandler` 負責把按鍵轉成 `GameEngine` 操作：

```text
Left  -> engine.moveLeft()
Right -> engine.moveRight()
Up    -> engine.rotate()
Down  -> engine.softDrop()
Space -> engine.hardDrop()
P     -> engine.togglePause()
R     -> engine.restart()
M     -> engine.toggleMusic()
```

每次有效按鍵後，`InputHandler` 會呼叫 `afterInput.run()`。在本專案中，這個 callback 會更新 Timer delay 並呼叫 `repaint()`。

這個分工讓程式比較清楚：

- `GamePanel`：註冊輸入處理器與重畫畫面。
- `InputHandler`：處理鍵盤事件。
- `GameEngine`：處理遊戲規則。
- `AudioManager`：處理音訊播放與缺檔保護。

未來如果要進一步貼近 Swing 慣例，可以把 `InputHandler` 從 `KeyListener` 改成 `InputMap` / `ActionMap` 的 Key Bindings 實作。

## GameEngine 與 AudioManager

`GameEngine` 是規則中心，知道何時發生 hard drop 或消行。這些事件發生時，它可以呼叫：

```java
audioManager.playDropSound();
audioManager.playLineClearSound();
```

目前 `drop.wav` 與 `clear.wav` 可以不存在；`AudioManager` 會安靜略過。背景音樂則透過 `AudioManager.toggleMusic()` 控制。

## GamePanel：顯示目前狀態

`GamePanel.paintComponent()` 只做繪圖：

1. 畫棋盤中已固定的方塊。
2. 畫目前正在下落的方塊。
3. 畫格線。
4. 畫右側資訊面板：Next、Score、Lines、Level、Music。
5. 畫操作提示：Arrow keys、Space、P、R、M。
6. 如果狀態是 `PAUSED`，畫暫停 overlay。
7. 如果狀態是 `GAME_OVER`，畫 Game Over overlay。

Music 顯示規則：

```text
katusha.mid 不存在 -> unavailable
katusha.mid 存在但未播放 -> off
katusha.mid 正在播放 -> on
```

## repaint() 與 paintComponent()

`repaint()` 不會立刻畫圖。它只是通知 Swing：「這個元件需要重畫」。

Swing 之後會在 Event Dispatch Thread 呼叫：

```java
protected void paintComponent(Graphics g)
```

所有畫面更新都應該集中在 `paintComponent()`。`Timer`、`InputHandler` 與 `AudioManager` 都不應該直接拿 `Graphics` 畫圖。

## 整體流程

```text
Timer 觸發
  -> GameEngine.tick()
  -> GameEngine 根據 GameState 決定是否更新
  -> 若消行，GameEngine 呼叫 AudioManager.playLineClearSound()
  -> GamePanel 更新 Timer delay
  -> repaint()
  -> paintComponent()

玩家按鍵
  -> InputHandler.keyPressed()
  -> 呼叫 GameEngine 對應方法
  -> 若按 M，GameEngine 呼叫 AudioManager.toggleMusic()
  -> afterInput 更新 Timer delay 並 repaint()
  -> paintComponent()
```
