package ee.taltech.onlineradio

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class RadioService : Service() {

    companion object {
        private val TAG = this::class.java.declaringClass!!.simpleName
    }

    private lateinit var mediaPlayer: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(Intent(C.ACTION_AUDIO_BUFFERING))

        mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse("https://kadi.babahhcdn.com/kadi"))
        mediaPlayer.start()

        if (mediaPlayer.isPlaying) {
            LocalBroadcastManager.getInstance(applicationContext)
                .sendBroadcast(Intent(C.ACTION_AUDIO_PLAYING))
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(Intent(C.ACTION_AUDIO_STOPPED))
        mediaPlayer.release()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}