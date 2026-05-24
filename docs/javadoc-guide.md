# Javadoc API 網頁教學

這份文件說明如何產生本專案的 API 網頁，以及如何閱讀產生出的文件。

## 什麼是 Javadoc

Javadoc 是 Java 內建的文件產生工具。它會讀取 `.java` 原始碼，產生 HTML 格式的 API 文件。

產生後可以用瀏覽器打開：

```text
docs/api/index.html
```

## 產生 API 文件

在專案根目錄執行：

```powershell
javadoc -d docs\api -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -windowtitle "Java Swing Tetris API" -doctitle "Java Swing Tetris API" src\tetris\*.java
```

參數意思：

- `-d docs\api`：輸出到 `docs/api/`。
- `-encoding UTF-8`：用 UTF-8 讀取原始碼。
- `-charset UTF-8`：HTML 頁面使用 UTF-8。
- `-docencoding UTF-8`：輸出的文件內容使用 UTF-8。
- `-windowtitle`：瀏覽器標題。
- `-doctitle`：文件頁面標題。
- `src\tetris\*.java`：要產生文件的來源檔。

## 閱讀入口

主要入口：

```text
docs/api/index.html
```

常用頁面：

- `docs/api/index.html`：首頁。
- `docs/api/allclasses-index.html`：所有 class 和 enum。
- `docs/api/index-all.html`：所有 method、field、class 的索引。
- `docs/api/tetris/GameEngine.html`：遊戲規則核心。
- `docs/api/tetris/AudioManager.html`：音訊播放管理。
- `docs/api/tetris/GamePanel.html`：Swing 畫面繪製。

## no comment warning 是什麼

產生時可能看到：

```text
warning: no comment
```

這不是錯誤。它只是提醒某個 class、method 或 field 沒有 Javadoc 註解。

如果只想產生 API 結構，這些 warning 可以先接受。若要做成更完整的教學文件，可以逐步補上註解。

## Javadoc 註解範例

Javadoc 註解使用 `/** ... */`：

```java
/**
 * Moves the current piece to the bottom and locks it on the board.
 */
public void hardDrop() {
    ...
}
```

如果方法有參數或回傳值，可以寫：

```java
/**
 * Returns whether the given tetromino can be placed on the board.
 *
 * @param tetromino the piece to test
 * @return true if the piece fits within the board and does not overlap locked cells
 */
public boolean canPlace(Tetromino tetromino) {
    ...
}
```

## 教學建議

可以讓學生先看 `GameEngine`，理解規則如何集中在一個 class 裡；再看 `GamePanel`，理解畫面如何依照 engine 狀態重畫；最後看 `AudioManager`，理解為什麼打包成 jar 後要從 classpath 讀 resource。
