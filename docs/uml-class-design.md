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
  責任：處理 Swing 畫面、Timer game loop、鍵盤事件與 repaint。

GameEngine
  - Board board
  - Tetromino currentPiece
  - boolean gameOver
  + tick()
  + moveLeft()
  + moveRight()
  + moveDown()
  + rotate()
  責任：管理遊戲規則，例如下落、移動、旋轉、固定方塊與產生新方塊。

Board
  + WIDTH = 10
  + HEIGHT = 20
  - Color[][] cells
  + canPlace(Tetromino tetromino)
  + lock(Tetromino tetromino)
  + getCell(int row, int col)
  責任：保存棋盤狀態，判斷方塊是否可放置，將方塊固定到棋盤。

Tetromino
  - Shape shape
  - int[][] blocks
  - int x
  - int y
  + movedBy(int dx, int dy)
  + rotatedClockwise()
  + getBlocks()
  責任：表示目前方塊的位置、形狀與旋轉後的格子資料。

Shape enum
  I, O, T, S, Z, J, L
  - int[][] blocks
  - Color color
  責任：定義七種 Tetris 方塊的初始格子座標與顏色。
```

## 關係摘要

```text
TetrisApp -> GameFrame -> GamePanel -> GameEngine
GameEngine -> Board
GameEngine -> Tetromino
Tetromino -> Shape
Board 使用 Tetromino 判斷碰撞與固定方塊
GamePanel 讀取 Board 與 Tetromino 來繪製畫面
```
