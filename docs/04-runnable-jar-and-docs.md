# Version 4: Runnable Jar And Documentation

Version 4 的目標是把前一版的 audio-ready 架構落實成可以移動、可以展示、也比較適合教學交付的版本。

這一版完成三件事：

- 產生可以直接執行的 `dist/tetris.jar`。
- 將 MIDI 與 WAV 音檔一起包進 jar。
- 補上 Javadoc API 網頁與教學文件。

## Runnable Jar

前一版可以用原始碼編譯後執行：

```powershell
javac -d out src/tetris/*.java
java -cp out tetris.TetrisApp
```

Version 4 另外提供 runnable jar：

```powershell
java -jar dist\tetris.jar
```

這樣把 `dist/tetris.jar` 移到其他電腦時，只要該電腦有 Java Runtime，就不需要重新 compile。

## Audio Assets Inside Jar

jar 內包含：

```text
assets/audio/katusha.mid
assets/audio/drop.wav
assets/audio/clear.wav
```

用途：

- `katusha.mid`：背景音樂，按 `M` 切換。
- `drop.wav`：hard drop 時播放。
- `clear.wav`：消除一行以上時播放。

## Classpath Resource Loading

打包前，音檔可以當成一般檔案讀取：

```text
assets/audio/drop.wav
```

打包進 jar 後，音檔變成 jar 內部的 resource，不再適合只用 `new File(...)` 讀取。

因此 `AudioManager` 現在會先從 classpath 找資源：

```java
AudioManager.class.getClassLoader().getResource(path)
```

如果 classpath 找不到，才回退到檔案系統。這讓同一份程式同時支援：

- 開發模式：直接從專案資料夾執行。
- 發布模式：從 `dist/tetris.jar` 執行。

## Sound Effect Behavior

音效觸發點集中在 `GameEngine`：

```text
Space hard drop
  -> GameEngine.hardDrop()
  -> AudioManager.playDropSound()
```

```text
Piece locked
  -> Board.clearCompletedLines()
  -> if clearedLines > 0
       AudioManager.playLineClearSound()
```

這表示 `clear.wav` 不是每次方塊落地都播放，只有真的消行才播放。

## Music Tempo By Level

`AudioManager.setMusicLevel()` 會依照目前等級調整 MIDI tempo factor。

呼叫時機：

- `restart()` 後重設音樂速度。
- 每次 piece lock 後依照最新等級更新速度。

## Javadoc API

Version 4 產生了 API 網頁：

```text
docs/api/index.html
```

產生命令：

```powershell
javadoc -d docs\api -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -windowtitle "Java Swing Tetris API" -doctitle "Java Swing Tetris API" src\tetris\*.java
```

如果看到 `warning: no comment`，代表 class 或 method 還沒有 Javadoc 註解。這不是產生失敗，只是提醒文件內容可以再補強。

## New Teaching Documents

Version 4 也補上三份教學文件：

```text
docs/how-to-package-jar.md
docs/audio-assets-guide.md
docs/javadoc-guide.md
```

各自用途：

- `how-to-package-jar.md`：說明如何編譯、打包 runnable jar，並檢查 jar 內容。
- `audio-assets-guide.md`：說明 MIDI/WAV 差異、Java 支援的 WAV 格式，以及 Audacity 轉檔。
- `javadoc-guide.md`：說明如何產生與閱讀 Javadoc API 網頁。

## Teaching Focus

這一版很適合用來說明「從可執行程式到可交付成果」的差別。

學生可以觀察到：

- 原始碼執行和 jar 執行的差異。
- 檔案系統路徑和 classpath resource 的差異。
- 音訊素材格式會影響 Java 是否能播放。
- Javadoc 可以把程式結構轉成可閱讀的 API 文件。
- Git commit 可以依照主題拆分，例如 jar 打包、API 文件、教學文件。
