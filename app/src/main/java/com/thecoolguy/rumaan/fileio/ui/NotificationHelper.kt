package com.thecoolguy.rumaan.fileio.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

class NotificationHelper {
    companion object {
        const val CHANNEL_ID = "42"
        const val NOTIFICATION_ID = 44
    }

    fun create(context: Context, fileEntity: FileEntity) {

        val intent = Intent(context, UploadHistoryActivity::class.java)

        // set the data into the intent
        intent.putExtra(context.getString(R.string.key_file_name), fileEntity.name)
        intent.putExtra(context.getString(R.string.key_file_url), fileEntity.url)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context,
                0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .apply {
                    setAutoCancel(true)
                    setSmallIcon(R.drawable.ic_file_upload)
                    setContentTitle("File Upload Successful")
                    setContentText(fileEntity.url)
                    priority = NotificationCompat.PRIORITY_DEFAULT
                    setContentIntent(pendingIntent)
                    setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                }

        // for O and up create a notification channel before posting the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "File Upload"
            val channelDescription = "Show notification when a file upload is successful"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
                    .apply {
                        description = channelDescription
                    }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build())

    }
}