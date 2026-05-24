# Runnable Jar 打包教學

這份文件說明如何把 Java Swing Tetris 打包成可以直接執行的 jar，並且把音訊素材一起放進 jar 裡。

## 目標

完成後會得到：

```text
dist/tetris.jar
```

執行方式：

```powershell
java -jar dist\tetris.jar
```

只要目標電腦已安裝 Java，就可以把 `tetris.jar` 複製到其他地方執行，不需要重新 compile。

## 編譯

先把 Java 原始碼編譯到 `out/`：

```powershell
javac -d out src/tetris/*.java
```

這會產生：

```text
out/tetris/*.class
```

## 打包

用 `jar cfe` 建立 runnable jar：

```powershell
jar cfe dist\tetris.jar tetris.TetrisApp -C out . assets\audio
```

參數意思：

- `c`：create，建立 jar。
- `f`：指定輸出檔案。
- `e`：指定進入點，也就是 main class。
- `dist\tetris.jar`：輸出的 jar 檔。
- `tetris.TetrisApp`：程式入口。
- `-C out .`：把 `out/` 裡的 class 檔放進 jar。
- `assets\audio`：把音訊素材資料夾也放進 jar。

## 為什麼音檔要改成 classpath 載入

開發時可以用檔案路徑讀取音檔：

```text
assets/audio/drop.wav
```

但是打包進 jar 之後，音檔不再是一般檔案系統上的檔案，而是 jar 內部的 resource。這時候要用 class loader 讀：

```java
AudioManager.class.getClassLoader().getResource(path)
```

目前 `AudioManager` 的策略是：

1. 先從 jar/classpath 內找音檔。
2. 找不到時，再回頭從檔案系統找。

因此同一份程式可以同時支援：

- 開發模式：`javac -d out src/tetris/*.java` 後執行。
- 發布模式：`java -jar dist\tetris.jar`。

## 檢查 jar 內容

可以用這個指令確認 jar 內有 class 和音檔：

```powershell
jar tf dist\tetris.jar
```

應該可以看到類似：

```text
tetris/TetrisApp.class
tetris/AudioManager.class
assets/audio/katusha.mid
assets/audio/drop.wav
assets/audio/clear.wav
```

## 常見問題

如果出現 `no main manifest attribute`，代表 jar 沒有設定 main class。請確認使用的是 `jar cfe`，而不是只有 `jar cf`。

如果遊戲可以開，但沒有音效，請先確認 jar 內有 `assets/audio/drop.wav` 和 `assets/audio/clear.wav`，再確認 WAV 格式是否為 Java 支援的 PCM WAV。

如果目標電腦顯示找不到 `java`，代表尚未安裝 Java，或 Java 沒有加入系統 PATH。
