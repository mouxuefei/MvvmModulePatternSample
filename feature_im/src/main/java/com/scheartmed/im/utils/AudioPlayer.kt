package com.scheartmed.im.utils

import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.*
import java.io.IOException

object AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var job: Job? = null

    /**
     * 播放音频
     * @param url 音频 URL 或本地路径
     * @param onStart 播放开始回调
     * @param onComplete 播放完成回调
     * @param onError 播放失败回调
     */
    fun play(
        url: String,
        onStart: (() -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null
    ) {
        stop() // 先停止上一个播放

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                    onStart?.invoke()
                }
                setOnCompletionListener {
                    onComplete?.invoke()
                    resetPlayer()
                }
                setOnErrorListener { _, what, extra ->
                    onError?.invoke(Exception("MediaPlayer Error: what=$what, extra=$extra"))
                    resetPlayer()
                    true
                }
                prepareAsync()
            }
        } catch (e: IOException) {
            onError?.invoke(e)
            e.printStackTrace()
        }
    }

    /**
     * 使用协程挂起函数播放音频
     */
    suspend fun playAsync(url: String) = withContext(Dispatchers.Main) {
        val deferred = CompletableDeferred<Unit>()
        play(
            url,
            onStart = { /* 可选 */ },
            onComplete = { deferred.complete(Unit) },
            onError = { deferred.completeExceptionally(it) }
        )
        deferred.await()
    }

    /**
     * 停止播放
     */
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            resetPlayer()
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        job?.cancel()
        job = null
    }

    private fun resetPlayer() {
        mediaPlayer?.reset()
    }
}