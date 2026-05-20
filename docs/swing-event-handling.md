# Swing Event Handling 說明

Version 3 的事件流程仍維持 Version 2 的分工：`Timer` 推動時間，`InputHandler` 接收鍵盤，`GameEngine` 更新規則狀態，`GamePanel` 負責畫面。新增的音訊功能由 `AudioManager` 管理。

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

## InputHandler：集中鍵盤操作

`InputHandler` 把鍵盤事件轉成 `GameEngine` 的方法呼叫：

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

`M` 鍵不直接處理 MIDI，也不碰 Swing 畫圖。它只呼叫 `GameEngine.toggleMusic()`，再由 `GameEngine` 轉交給 `AudioManager`。

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

所有畫面更新都應該集中在 `paintComponent()`。Timer、InputHandler 與 AudioManager 都不應該直接拿 `Graphics` 畫圖。

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
