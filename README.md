# Java Swing Tetris

這是一個教學用 Java Swing Tetris 專案。Version 2 在 Version 1 的最小可玩版本上，加入消行、分數、等級、下一個方塊預覽、暫停、重新開始與更完整的狀態顯示。

## 專案結構

```text
src/tetris/
  TetrisApp.java
  GameFrame.java
  GamePanel.java
  Board.java
  Tetromino.java
  Shape.java
  GameEngine.java
  GameState.java
  ScoreManager.java
  InputHandler.java
docs/
  01-minimum-playable-version.md
  02-rules-and-refactoring.md
  uml-class-design.md
  swing-event-handling.md
```

## 在 VS Code 中編譯與執行

請先確認已安裝 JDK，並且 `javac`、`java` 可以在終端機中使用。

1. 在 VS Code 開啟此資料夾。
2. 開啟 Terminal。
3. 編譯：

```powershell
javac -d out src/tetris/*.java
```

4. 執行：

```powershell
java -cp out tetris.TetrisApp
```

## 操作方式

- Left：向左移動
- Right：向右移動
- Up：順時針旋轉
- Down：soft drop，向下移動一格
- Space：hard drop，直接落到底
- P：暫停 / 繼續
- R：重新開始

## Version 2 功能

- 棋盤大小 10 x 20。
- 方塊會自動下落，並可左右移動、旋轉、soft drop、hard drop。
- 方塊固定後會檢查並清除完整橫列。
- `ScoreManager` 管理分數、消行數與等級。
- 等級每 10 行提高一次，等級越高下落速度越快。
- 右側顯示下一個方塊、分數、消行數、等級與快捷鍵。
- `GameState` 管理 `RUNNING`、`PAUSED`、`GAME_OVER`。
- Game Over 畫面顯示最終分數與重新開始提示。

本專案仍刻意維持教學用簡潔架構，未加入音樂、音效、開始畫面或複雜 UI。
