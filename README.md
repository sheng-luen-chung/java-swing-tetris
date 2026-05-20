# Java Swing Tetris

這是一個教學用 Java Swing Tetris 專案。Version 1 只實作 Minimum Playable Tetris：方塊會下落、可以移動與旋轉、會碰撞並固定到棋盤，新方塊無法產生時顯示 Game Over。

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
docs/
  01-minimum-playable-version.md
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
- Down：加速下落一格
- Up：順時針旋轉

## Version 1 範圍

本版本不包含分數、消行、音樂、音效、開始畫面或複雜 UI。目標是用最少的程式碼建立一個容易閱讀、容易教學、可以玩的基礎版本。
