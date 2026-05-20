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
  責任：顯示棋盤、方塊、下一個方塊、分數、等級、音樂狀態、操作提示與 overlay。

InputHandler extends KeyAdapter
  - GameEngine engine
  - Runnable afterInput
  + keyPressed(KeyEvent event)
  責任：集中處理鍵盤輸入，包含移動、旋轉、hard drop、pause、restart 與 music toggle。

GameEngine
  - Board board
  - ScoreManager scoreManager
  - AudioManager audioManager
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
  + toggleMusic()
  + getDropDelay()
  責任：管理遊戲規則、狀態切換、速度、消行後音效與音樂控制入口。

AudioManager
  - Sequencer sequencer
  - boolean musicAvailable
  - boolean musicEnabled
  + playBackgroundMusic()
  + stopBackgroundMusic()
  + toggleMusic()
  + isMusicEnabled()
  + isMusicAvailable()
  + playLineClearSound()
  + playDropSound()
  責任：使用 Java 標準音訊 API 管理 MIDI 背景音樂與未來 WAV 音效擴充點。

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
GameEngine -> AudioManager
GameEngine -> GameState
GameEngine -> Tetromino
Tetromino -> Shape
```
