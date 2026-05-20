# Version 2：Rules and Refactoring

Version 2 在 Version 1 的 Minimum Playable Tetris 上，加入更完整的遊戲規則，同時把職責重新整理得更清楚。

## 相對 Version 1 改了什麼

- 新增消行功能：方塊固定後，棋盤會清除已填滿的橫列。
- 新增分數：一次消除越多行，得分越高。
- 新增等級：每消除 10 行提升 1 級。
- 新增速度調整：等級越高，Timer delay 越短，方塊下落越快。
- 新增下一個方塊預覽。
- 新增暫停 / 繼續。
- 新增重新開始。
- 改善 Game Over 顯示，加入最終分數與重新開始提示。
- 新增 `InputHandler`，避免鍵盤事件散落在 `GamePanel`。
- 新增 `GameState`，用 enum 表示流程狀態。
- 新增 `ScoreManager`，讓分數與等級計算離開畫面層。

## 新增 class 的責任

`GameState`：

用 `RUNNING`、`PAUSED`、`GAME_OVER` 表示遊戲流程。這比 `boolean gameOver`、`boolean paused` 更清楚，因為同一時間只會有一種狀態。

`ScoreManager`：

保存並計算 `score`、`linesCleared`、`level`。當 `Board` 回傳本次清除幾行後，`GameEngine` 把行數交給 `ScoreManager` 更新。

`InputHandler`：

集中處理鍵盤事件。它把 Left、Right、Up、Down、Space、P、R 轉成 `GameEngine` 的方法呼叫，讓 `GamePanel` 不需要放一大段 switch。

## 為什麼 ScoreManager 不應該塞在 GamePanel

`GamePanel` 的責任是 Swing 顯示：畫棋盤、畫方塊、畫分數文字、畫 overlay。

分數與等級屬於遊戲規則，不屬於畫面。若把計分邏輯放在 `GamePanel`，會造成幾個問題：

- 之後想改分數規則時，必須修改畫面程式。
- 測試分數計算會變困難，因為它和 Swing 元件綁在一起。
- `GameEngine` 無法完整掌握遊戲進度，例如速度要根據等級改變時會變得彆扭。

因此 Version 2 使用 `ScoreManager` 管理分數，`GamePanel` 只讀取並顯示結果。

## GameState 如何讓遊戲流程更清楚

Version 1 只有 `gameOver` boolean。Version 2 需要暫停、繼續、重新開始與 Game Over。如果繼續增加 boolean，很容易出現矛盾狀態，例如 `paused == true` 且 `gameOver == true`。

使用 `GameState` 後，流程變成：

```text
RUNNING  -> 玩家按 P -> PAUSED
PAUSED   -> 玩家按 P -> RUNNING
RUNNING  -> 新方塊無法放置 -> GAME_OVER
任何狀態 -> 玩家按 R -> RUNNING
```

`GameEngine.tick()` 只在 `RUNNING` 時更新規則，流程判斷會更直接。

## Timer、InputHandler、GameEngine、GamePanel 的互動

```text
Timer
  -> 呼叫 GameEngine.tick()
  -> 根據 level 更新 Timer delay
  -> 呼叫 repaint()
  -> GamePanel.paintComponent() 畫出最新狀態

InputHandler
  -> 收到玩家按鍵
  -> 呼叫 GameEngine 的移動、旋轉、暫停或重新開始方法
  -> 呼叫 afterInput
  -> GamePanel 更新 Timer delay 並 repaint()
```

核心原則是：`GameEngine` 改變狀態，`GamePanel` 顯示狀態，`InputHandler` 只負責把鍵盤輸入轉成操作。
