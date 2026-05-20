# 文字版 UML Class Design

```text
TetrisApp
  + main(String[] args)
  責任：程式進入點，在 Swing Event Dispatch Thread 建立 GameFrame。

GameFrame extends JFrame
  + GameFrame()
  責任：設定主視窗標題、關閉行為、大小與 GamePanel。

GamePanel extends JPanel
  - GameEngine engine
  - Timer timer
  + paintComponent(Graphics g)
  責任：顯示棋盤、目前方塊、下一個方塊、分數、等級、Paused 與 Game Over overlay。

InputHandler extends KeyAdapter
  - GameEngine engine
  - Runnable afterInput
  + keyPressed(KeyEvent event)
  責任：集中處理鍵盤輸入，將按鍵轉成 GameEngine 的操作。

GameEngine
  - Board board
  - ScoreManager scoreManager
  - Tetromino currentPiece
  - Tetromino nextPiece
  - GameState state
  + tick()
  + moveLeft()
  + moveRight()
  + softDrop()
  + hardDrop()
  + rotate()
  + togglePause()
  + restart()
  + getDropDelay()
  責任：管理遊戲規則，例如下落、移動、旋轉、固定、消行、產生方塊、狀態切換與速度。

GameState enum
  RUNNING, PAUSED, GAME_OVER
  責任：明確表示目前遊戲流程狀態，避免用多個 boolean 混合判斷。

ScoreManager
  - int score
  - int linesCleared
  - int level
  + addClearedLines(int lines)
  + reset()
  責任：管理分數、消行數與等級計算。

Board
  + WIDTH = 10
  + HEIGHT = 20
  - Color[][] cells
  + clear()
  + canPlace(Tetromino tetromino)
  + lock(Tetromino tetromino)
  + clearCompletedLines()
  + getCell(int row, int col)
  責任：保存棋盤狀態，判斷碰撞，固定方塊，清除完整橫列。

Tetromino
  - Shape shape
  - int[][] blocks
  - int x
  - int y
  + movedBy(int dx, int dy)
  + rotatedClockwise()
  + getBlocks()
  責任：表示一個方塊的位置、形狀與旋轉後的格子資料。

Shape enum
  I, O, T, S, Z, J, L
  - int[][] blocks
  - Color color
  責任：定義七種 Tetris 方塊的初始格子座標與顏色。
```

## 關係摘要

```text
TetrisApp -> GameFrame -> GamePanel
GamePanel -> GameEngine
GamePanel -> InputHandler
InputHandler -> GameEngine
GameEngine -> Board
GameEngine -> ScoreManager
GameEngine -> GameState
GameEngine -> Tetromino
Tetromino -> Shape
```
