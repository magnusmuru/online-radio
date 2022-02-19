package ee.taltech.onlineradio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = this::class.java.declaringClass!!.simpleName
    }

    private lateinit var buttonPlayStop: Button
    lateinit var radioProgressBar: ProgressBar
    private var flagPlaying = false
    private var localReceiver = BroadcastReceiverInMainActivity()

    private val localReceiverIntentFilter = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonPlayStop = findViewById(R.id.buttonPlay)
        radioProgressBar = findViewById(R.id.radioProgress)

        localReceiverIntentFilter.addAction(C.ACTION_AUDIO_PLAYING)
        localReceiverIntentFilter.addAction(C.ACTION_AUDIO_BUFFERING)
        localReceiverIntentFilter.addAction(C.ACTION_AUDIO_STOPPED)
    }

    fun buttonRadioHandler(view: View) {
        val intent = Intent(applicationContext, RadioService::class.java)
        buttonPlayStop.text = "BUFFERING"
        radioProgressBar.visibility = View.VISIBLE
        flagPlaying = if (!flagPlaying) {
            startService(intent)
            true
        } else {
            stopService(intent)
            false
        }
    }

    fun pickRandomMessage(view: View) {

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(localReceiver, localReceiverIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(applicationContext, RadioService::class.java)
        stopService(serviceIntent);
    }

    private inner class BroadcastReceiverInMainActivity : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(
                TAG,
                "BroadcastReceiverInMainActivity.onReceive " + (intent?.action ?: "null intent")
            )
            when (intent?.action) {
                C.ACTION_AUDIO_BUFFERING -> {
                    // This does absolutely nothing. I assume this because messages are going out on service start
                    buttonPlayStop.text = "BUFFERING"
                }
                C.ACTION_AUDIO_PLAYING -> {
                    buttonPlayStop.text = "STOP"
                    radioProgressBar.visibility = View.INVISIBLE
                }
                C.ACTION_AUDIO_STOPPED -> {
                    buttonPlayStop.text = "PLAY"
                    radioProgressBar.visibility = View.INVISIBLE
                }

            }
        }
    }
}