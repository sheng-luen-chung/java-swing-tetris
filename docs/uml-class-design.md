# UML Class Design

這份文件使用 Mermaid class diagram 描述目前 Version 3 的 class 關係。若在 GitHub 或支援 Mermaid 的 VS Code Markdown preview 中開啟，下面區塊會被渲染成 UML 圖。

## Mermaid Diagram

```mermaid
classDiagram
    class TetrisApp {
        +main(String[] args) void
    }

    class GameFrame {
        +GameFrame()
    }

    class GamePanel {
        -GameEngine engine
        -Timer timer
        +addNotify() void
        #paintComponent(Graphics g) void
    }

    class InputHandler {
        -GameEngine engine
        -Runnable afterInput
        +keyPressed(KeyEvent event) void
    }

    class GameEngine {
        -Board board
        -ScoreManager scoreManager
        -AudioManager audioManager
        -Random random
        -Tetromino currentPiece
        -Tetromino nextPiece
        -GameState state
        +GameEngine()
        +tick() void
        +moveLeft() void
        +moveRight() void
        +softDrop() void
        +hardDrop() void
        +rotate() void
        +togglePause() void
        +restart() void
        +toggleMusic() void
        +getDropDelay() int
        +getBoard() Board
        +getCurrentPiece() Tetromino
        +getNextPiece() Tetromino
        +getScoreManager() ScoreManager
        +getState() GameState
        +getAudioManager() AudioManager
        +isGameOver() boolean
    }

    class AudioManager {
        -String BACKGROUND_MUSIC_PATH
        -String LINE_CLEAR_SOUND_PATH
        -String DROP_SOUND_PATH
        -Sequencer sequencer
        -boolean musicAvailable
        -boolean musicEnabled
        -boolean missingMusicWarningShown
        -float tempoFactor
        +AudioManager()
        +playBackgroundMusic() void
        +stopBackgroundMusic() void
        +toggleMusic() void
        +setMusicLevel(int level) void
        +isMusicEnabled() boolean
        +isMusicAvailable() boolean
        +playLineClearSound() void
        +playDropSound() void
        -loadMidiSequence(String path) Sequence
        -loadAudioInputStream(String path) AudioInputStream
        -resourceExists(String path) boolean
    }

    class Board {
        +WIDTH int
        +HEIGHT int
        -Color[][] cells
        +clear() void
        +canPlace(Tetromino tetromino) boolean
        +lock(Tetromino tetromino) void
        +clearCompletedLines() int
        +getCell(int row, int col) Color
    }

    class Tetromino {
        -Shape shape
        -int[][] blocks
        -int x
        -int y
        +movedBy(int dx, int dy) Tetromino
        +rotatedClockwise() Tetromino
        +getBlocks() int[][]
        +getColor() Color
        +getX() int
        +getY() int
    }

    class Shape {
        <<enumeration>>
        I
        O
        T
        S
        Z
        J
        L
        -int[][] blocks
        -Color color
        +getBlocks() int[][]
        +getColor() Color
    }

    class GameState {
        <<enumeration>>
        RUNNING
        PAUSED
        GAME_OVER
    }

    class ScoreManager {
        -int score
        -int linesCleared
        -int level
        +reset() void
        +addClearedLines(int lines) void
        +getScore() int
        +getLinesCleared() int
        +getLevel() int
    }

    JFrame <|-- GameFrame
    JPanel <|-- GamePanel
    KeyAdapter <|-- InputHandler

    TetrisApp --> GameFrame : creates
    GameFrame --> GamePanel : contains
    GamePanel --> GameEngine : renders state
    GamePanel --> InputHandler : registers
    InputHandler --> GameEngine : commands
    GameEngine *-- Board
    GameEngine *-- ScoreManager
    GameEngine *-- AudioManager
    GameEngine --> GameState
    GameEngine --> Tetromino
    Tetromino --> Shape
    Board --> Tetromino : validates / locks
```

## Class Responsibilities

`TetrisApp`：程式進入點，負責在 Swing Event Dispatch Thread 建立 `GameFrame`。

`GameFrame`：主視窗，負責設定標題、關閉行為、尺寸與放入 `GamePanel`。

`GamePanel`：畫面層，負責繪製棋盤、方塊、分數、等級、下一個方塊、音樂狀態、操作提示與 overlay。

`InputHandler`：輸入層，集中處理方向鍵、Space、P、R、M，並轉呼叫 `GameEngine`。

`GameEngine`：規則層，管理下落、移動、旋轉、hard drop、消行、分數、等級、狀態切換與音訊操作入口。

`AudioManager`：音訊層，負責 MIDI 背景音樂與未來 WAV 音效擴充點，並確保音檔缺失時遊戲不 crash。

`Board`：棋盤資料，保存固定方塊、判斷碰撞、清除完整行。

`Tetromino`：目前方塊的形狀、位置與旋轉後 block 座標。

`Shape`：七種 Tetromino 的初始 block 座標與顏色。

`GameState`：遊戲流程狀態，包含 `RUNNING`、`PAUSED`、`GAME_OVER`。

`ScoreManager`：分數、消行數與等級計算。

## Latest Version Notes

This diagram reflects the current audio-ready and runnable-jar version.

- `AudioManager` now supports MIDI background music, WAV sound effects, music tempo changes by level, and resource loading from either the jar/classpath or the local file system.
- `drop.wav` is played during `hardDrop()`.
- `clear.wav` is played only when `clearCompletedLines()` returns one or more cleared lines.
- `setMusicLevel()` is called after restart and after each piece lock so the MIDI tempo follows the current level.
- The runnable jar includes `assets/audio/katusha.mid`, `assets/audio/drop.wav`, and `assets/audio/clear.wav`.

## Use Case Diagram

Mermaid 沒有專用的 UML use case 語法，因此這裡用 flowchart 表示 actor 與 use case 的關係。

```mermaid
flowchart LR
    Player([Player])

    subgraph TetrisGame["Java Swing Tetris"]
        Move["Move piece"]
        Rotate["Rotate piece"]
        SoftDrop["Soft drop"]
        HardDrop["Hard drop"]
        Pause["Pause / Resume"]
        Restart["Restart game"]
        ViewInfo["View score / lines / level"]
        Preview["View next piece"]
        ToggleMusic["Toggle music"]
        GameOver["See Game Over"]
    end

    Player --> Move
    Player --> Rotate
    Player --> SoftDrop
    Player --> HardDrop
    Player --> Pause
    Player --> Restart
    Player --> ViewInfo
    Player --> Preview
    Player --> ToggleMusic
    Player --> GameOver

    HardDrop -. may trigger .-> GameOver
    Move -. uses .-> ViewInfo
    Rotate -. uses .-> Preview
    ToggleMusic -. depends on .-> ViewInfo
```

## Scenario Diagrams

以下 scenario 描述玩家操作如何流經 `InputHandler`、`GameEngine`、`AudioManager` 與 `GamePanel`。

### Scenario 1：Move Or Rotate Piece

```mermaid
sequenceDiagram
    actor Player
    participant InputHandler
    participant GameEngine
    participant Board
    participant GamePanel

    Player->>InputHandler: Press Arrow key
    InputHandler->>GameEngine: moveLeft() / moveRight() / rotate() / softDrop()
    GameEngine->>Board: canPlace(candidate)
    Board-->>GameEngine: true / false
    alt valid move
        GameEngine->>GameEngine: update currentPiece
    else invalid move
        GameEngine->>GameEngine: keep currentPiece
    end
    InputHandler->>GamePanel: afterInput callback
    GamePanel->>GamePanel: repaint()
```

### Scenario 2：Timer Tick And Line Clear

```mermaid
sequenceDiagram
    participant Timer
    participant GameEngine
    participant Board
    participant ScoreManager
    participant AudioManager
    participant GamePanel

    Timer->>GameEngine: tick()
    GameEngine->>Board: canPlace(currentPiece moved down)
    alt can move down
        GameEngine->>GameEngine: update currentPiece
    else cannot move down
        GameEngine->>Board: lock(currentPiece)
        GameEngine->>Board: clearCompletedLines()
        Board-->>GameEngine: clearedLines
        opt clearedLines > 0
            GameEngine->>AudioManager: playLineClearSound()
        end
        GameEngine->>ScoreManager: addClearedLines(clearedLines)
        GameEngine->>AudioManager: setMusicLevel(level)
        GameEngine->>GameEngine: spawnNextPiece()
    end
    Timer->>GamePanel: repaint()
```

### Scenario 3：Hard Drop

```mermaid
sequenceDiagram
    actor Player
    participant InputHandler
    participant GameEngine
    participant Board
    participant AudioManager
    participant GamePanel

    Player->>InputHandler: Press Space
    InputHandler->>GameEngine: hardDrop()
    loop until next row is blocked
        GameEngine->>Board: canPlace(piece moved down)
        Board-->>GameEngine: true
        GameEngine->>GameEngine: move piece down
    end
    GameEngine->>AudioManager: playDropSound()
    GameEngine->>Board: lock(currentPiece)
    GameEngine->>Board: clearCompletedLines()
    opt clearedLines > 0
        GameEngine->>AudioManager: playLineClearSound()
    end
    GameEngine->>ScoreManager: addClearedLines(clearedLines)
    GameEngine->>AudioManager: setMusicLevel(level)
    InputHandler->>GamePanel: afterInput callback
    GamePanel->>GamePanel: repaint()
```

### Scenario 4：Pause, Restart, And Music Toggle

```mermaid
sequenceDiagram
    actor Player
    participant InputHandler
    participant GameEngine
    participant AudioManager
    participant GamePanel

    alt Press P
        Player->>InputHandler: Press P
        InputHandler->>GameEngine: togglePause()
        GameEngine->>GameEngine: RUNNING <-> PAUSED
    else Press R
        Player->>InputHandler: Press R
        InputHandler->>GameEngine: restart()
        GameEngine->>GameEngine: reset board, score, pieces, state
    else Press M
        Player->>InputHandler: Press M
        InputHandler->>GameEngine: toggleMusic()
        GameEngine->>AudioManager: toggleMusic()
        AudioManager->>AudioManager: play / stop / warn if unavailable
    end

    InputHandler->>GamePanel: afterInput callback
    GamePanel->>GamePanel: repaint()
```
