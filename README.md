# Java Swing Tetris

這是一個教學用 Java Swing Tetris 專案。專案包含可從原始碼編譯執行的 Swing 遊戲，也提供已打包好的 runnable jar。

目前版本已包含背景音樂與音效素材：

- `assets/audio/katusha.mid`：背景音樂，按 `M` 切換播放。
- `assets/audio/drop.wav`：hard drop 時播放。
- `assets/audio/clear.wav`：消除一行以上時播放。

`dist/tetris.jar` 已將 class 檔與音訊素材一起包入 jar。把 jar 移到其他有 Java 的電腦時，可以不用重新 compile 直接執行。

## 快速執行

如果只想執行已打包版本：

```powershell
java -jar dist\tetris.jar
```

如果要從原始碼編譯後執行：

```powershell
javac -d out src/tetris/*.java
java -cp out tetris.TetrisApp
```

建議使用 Java 8 以上。可以用以下指令確認：

```powershell
java -version
```

## 操作方式

- Left / Right：左右移動
- Up：旋轉
- Down：soft drop
- Space：hard drop
- P：pause / resume
- R：restart
- M：music on/off

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
  katusha.mid
  drop.wav
  clear.wav
  README.md

dist/
  tetris.jar

docs/
  01-minimum-playable-version.md
  02-rules-and-refactoring.md
  03-audio-ready-polish.md
  04-runnable-jar-and-docs.md
  audio-assets-guide.md
  how-to-package-jar.md
  javadoc-guide.md
  swing-event-handling.md
  uml-class-design.md
  api/
```

## 音訊設計

`AudioManager` 負責背景音樂與短音效。

開發模式下，音檔可以從專案資料夾讀取。打包成 jar 後，音檔會從 jar/classpath resource 讀取。這讓同一份程式可以同時支援：

- `java -cp out tetris.TetrisApp`
- `java -jar dist\tetris.jar`

如果音檔缺失或格式不支援，遊戲不會 crash，會在 console 顯示 warning。

## API 文件

Javadoc API 網頁入口：

```text
docs/api/index.html
```

重新產生 API 文件：

```powershell
javadoc -d docs\api -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -windowtitle "Java Swing Tetris API" -doctitle "Java Swing Tetris API" src\tetris\*.java
```

## 教學文件

- `docs/01-minimum-playable-version.md`：Version 1 最小可玩版本。
- `docs/02-rules-and-refactoring.md`：規則補完與重構。
- `docs/03-audio-ready-polish.md`：audio-ready 架構設計。
- `docs/04-runnable-jar-and-docs.md`：runnable jar、內嵌音檔、Javadoc 與教學文件整理。
- `docs/audio-assets-guide.md`：MIDI/WAV 差異、Java 支援的 WAV 格式與 Audacity 轉檔。
- `docs/how-to-package-jar.md`：如何產生 runnable jar 並把音檔一起包入 jar。
- `docs/javadoc-guide.md`：如何產生與閱讀 Javadoc API 網頁。
- `docs/swing-event-handling.md`：Swing 事件處理與鍵盤輸入流程。
- `docs/uml-class-design.md`：Class diagram、use case 與 scenario diagrams。

## 常見問題

如果 `java -jar dist\tetris.jar` 無法執行，先確認電腦是否已安裝 Java，並且 `java` 指令可用。

如果沒有音效，請確認執行的是最新的 `dist/tetris.jar`，或從原始碼執行時 `assets/audio/` 內有 `katusha.mid`、`drop.wav`、`clear.wav`。

如果自行替換 WAV 音效，建議使用 `WAV / PCM signed / 16-bit` 格式。只把 MP3 改副檔名成 `.wav` 不會變成真正的 WAV。
