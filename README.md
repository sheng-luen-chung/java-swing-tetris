# Java Swing Tetris

這是一個教學用 Java Swing Tetris 專案。Version 3 在 Version 2 的規則與 UI 基礎上，加入 audio-ready 架構：目前 repo 不附音樂檔，但未來只要放入合法取得或自行製作的 MIDI 檔，就可以用 `M` 鍵切換背景音樂。

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
  AudioManager.java
assets/audio/
  README.md
  .gitkeep
docs/
  01-minimum-playable-version.md
  02-rules-and-refactoring.md
  03-audio-ready-polish.md
  uml-class-design.md
  swing-event-handling.md
```

## 在 VS Code 中編譯與執行

請先確認已安裝 JDK，並且 `javac`、`java` 可以在終端機中使用。

```powershell
javac -d out src/tetris/*.java
java -cp out tetris.TetrisApp
```

## 操作方式

- Arrow keys：移動 / 旋轉
- Down：soft drop
- Space：hard drop
- P：pause / resume
- R：restart
- M：music on/off

## Version 3 功能

- 保留 Version 2 的消行、分數、等級、加速、下一個方塊預覽、暫停與重新開始。
- 新增 `AudioManager`，使用 Java 標準函式庫處理音訊。
- MIDI 背景音樂預設路徑為 `assets/audio/katusha.mid`。
- 如果 `katusha.mid` 不存在，console 會顯示 warning，遊戲仍正常執行。
- UI 會顯示 Music 狀態：`unavailable`、`off` 或 `on`。
- `clear.wav` 與 `drop.wav` 是未來音效擴充點，目前不存在也不會 crash。

本教學 repo 不直接附上 `katusha.mid`。若要啟用背景音樂，請自行放入合法取得或自行製作的：

```text
assets/audio/katusha.mid
```

## 補充教學文件

- `docs/how-to-package-jar.md`：說明如何產生 runnable jar，並把音檔一起包進 jar。
- `docs/audio-assets-guide.md`：說明 MIDI/WAV 差異、Java 支援的 WAV 格式，以及 Audacity 轉檔方式。
- `docs/javadoc-guide.md`：說明如何產生與閱讀 `docs/api/` API 網頁。
