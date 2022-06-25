package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener { download() }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.stop()
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: 0
            val query = DownloadManager.Query().apply { setFilterById(id) }
            val cursor = downloadManager.query(query)
            val urlParts = getSelectedUrl().split("/")

            var filename = getSelectedUrl()
            try { filename = urlParts[urlParts.size - 1] } catch (e: Exception) { }
            val option = findViewById<RadioButton>(optionsGroup.checkedRadioButtonId).text

            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex))
                    showSuccessMessage(filename, option.toString()
                ) else showErrorMessage(filename, option.toString())
            }
        }
    }

    private fun showSuccessMessage(filename: String, option: String) {
        Toast.makeText(
            this,
            getString(R.string.download_successful_message, filename, option),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showErrorMessage(filename: String, option: String) {
        Toast.makeText(
            this,
            getString(R.string.download_failed_message, filename, option),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getSelectedUrl(): String {
        val index: Int =
            optionsGroup.indexOfChild(findViewById(optionsGroup.checkedRadioButtonId))
        return URL[index]
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(getSelectedUrl()))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private val URL = listOf(
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip",
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        )
        private const val CHANNEL_ID = "channelId"
    }

}
