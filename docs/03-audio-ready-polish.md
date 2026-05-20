# Version 3：Audio-ready Polish

Version 3 的目標不是提供音樂檔，而是建立 audio-ready 架構。也就是說，repo 可以先沒有任何音訊素材，但程式已經準備好在未來播放 MIDI 背景音樂與 WAV 音效。

## AudioManager 的責任

`AudioManager` 集中處理音訊：

- 使用 `javax.sound.midi.Sequencer` 播放 MIDI 背景音樂。
- 預設背景音樂路徑是 `assets/audio/katusha.mid`。
- 提供 `playBackgroundMusic()`、`stopBackgroundMusic()`、`toggleMusic()`。
- 提供 `isMusicEnabled()` 與 `isMusicAvailable()` 讓 UI 顯示狀態。
- 保留 `playLineClearSound()` 與 `playDropSound()`，未來可播放 `clear.wav` 與 `drop.wav`。
- 所有音訊載入與播放都用 `try/catch` 保護，避免音檔問題讓遊戲中止。

## 為什麼音訊載入失敗不能讓遊戲中止

在 Swing 遊戲中，音訊是加分體驗，不應該是核心遊戲能不能玩的前提。玩家可能遇到：

- 音檔不存在。
- 音檔格式不支援。
- 系統沒有可用的 MIDI 裝置。
- 音訊裝置暫時被其他程式占用。

如果這些情況造成 crash，玩家會連基本 Tetris 都玩不到。因此 Version 3 的策略是：console 顯示 warning，UI 顯示 `Music: unavailable` 或 `Music: off`，遊戲繼續執行。

## MIDI、WAV 與 PDF 樂譜的差異

MIDI 檔：

MIDI 是音樂事件資料，例如音高、節奏、樂器與速度。它不是錄音，而是讓系統或軟體音源依照資料播放音樂。Version 3 使用 `javax.sound.midi.Sequencer` 播放 `.mid`。

WAV 音效：

WAV 通常是實際錄音或已渲染的音訊波形，適合短音效，例如消行、落地、按鍵聲。Version 3 先保留 `javax.sound.sampled.Clip` 的播放骨架。

PDF 樂譜：

PDF 樂譜是給人看的文件，不是可直接播放的遊戲音訊。就算有 PDF 樂譜，也需要合法地編曲、製作 MIDI 或錄音後，遊戲才能播放。

## 未來如何加入 katusha.mid

本 repo 不直接附上 `katusha.mid`，因為音樂可能涉及作曲、編曲、錄音與散布授權。

未來若要啟用背景音樂，請自行放入合法取得或自行製作的檔案：

```text
assets/audio/katusha.mid
```

接著重新執行遊戲，按 `M` 即可切換背景音樂 on/off。

## M 鍵如何呼叫 AudioManager

事件流程如下：

```text
玩家按 M
  -> InputHandler.keyPressed()
  -> engine.toggleMusic()
  -> GameEngine 呼叫 audioManager.toggleMusic()
  -> AudioManager 檢查 katusha.mid 是否存在
  -> 存在就播放或停止，不存在就顯示 warning
  -> GamePanel.repaint()
  -> 右側資訊顯示 Music 狀態
```

這個流程保留了清楚分工：`InputHandler` 處理按鍵，`GameEngine` 提供遊戲操作入口，`AudioManager` 處理音訊細節，`GamePanel` 顯示結果。
