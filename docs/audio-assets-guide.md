# Audio Assets 教學

這份文件說明本專案使用的音訊檔案、Java 支援的格式，以及如何避免「副檔名是 wav，但其實不能播放」的問題。

## 音檔位置

本專案預設使用：

```text
assets/audio/katusha.mid
assets/audio/drop.wav
assets/audio/clear.wav
```

用途：

- `katusha.mid`：背景音樂，按 `M` 切換播放。
- `drop.wav`：按 `Space` hard drop 時播放。
- `clear.wav`：成功消除一行以上時播放。

## MIDI 和 WAV 的差別

MIDI 不是錄音檔。它比較像樂譜，記錄音高、節奏、樂器等資訊，播放時由系統的 MIDI 合成器發聲。

WAV 通常是實際音訊波形，適合短音效，例如落地、消行、按鈕聲。

本專案用：

- `javax.sound.midi.Sequencer` 播放 MIDI。
- `javax.sound.sampled.Clip` 播放 WAV。

## .wav 不一定是真的 WAV

副檔名不能保證格式正確。常見錯誤包含：

- 把 MP3 改名成 `.wav`。
- 從瀏覽器另存成網頁快照，檔名卻叫 `.wav`。
- WAV 使用 Java 不支援的編碼。

真正常見的 WAV 檔開頭會包含：

```text
RIFF
WAVE
```

如果檔案開頭是 `FF FB`、`FF F3` 或看到 `LAME`，通常是 MP3，不是真正的 WAV。

## 建議格式

為了讓 Java 內建音訊 API 穩定播放，建議使用：

```text
WAV
PCM signed
16-bit
44100 Hz 或 32000 Hz
mono 或 stereo
```

## 用 Audacity 轉檔

Audacity 是免費的音訊編輯軟體，可以把 MP3 或其他音訊轉成真正的 PCM WAV。

步驟：

1. 打開 Audacity。
2. 把音檔拖進 Audacity。
3. 選 `File`。
4. 選 `Export Audio...`。
5. Format 選 `WAV (Microsoft)`。
6. Encoding 選 `Signed 16-bit PCM`。
7. 存成 `drop.wav` 或 `clear.wav`。
8. 放到 `assets/audio/`。

## 測試時要注意

`drop.wav` 只有按 `Space` hard drop 時會播放。

`clear.wav` 只有真的消除一行以上時會播放。如果只是方塊落下但沒有消行，不會播放 clear 音效。

## 程式如何避免音檔不存在就 crash

`AudioManager` 播放前會先檢查 resource 是否存在。如果找不到音檔，程式會安靜略過，不會讓遊戲停止。

如果音檔存在但格式錯誤，console 會印出 warning，例如：

```text
Warning: could not play sound effect assets/audio/clear.wav: File of unsupported format
```
