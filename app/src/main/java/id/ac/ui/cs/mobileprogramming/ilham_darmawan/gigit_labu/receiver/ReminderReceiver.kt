package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.MainActivity
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.R

// Receiver to handle reminder notification
class ReminderReceiver: BroadcastReceiver() {
    private var channelId = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        // set notification content and behavior
        val currentIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context!!, 0, currentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder =
            NotificationCompat.Builder(context, context.resources.getString(R.string.channel_id))
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(context.resources.getString(R.string.reminder_title))
                .setContentText(context.resources.getString(R.string.reminder_message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true)
        with(NotificationManagerCompat.from(context.applicationContext)) {
            // notificationId is a unique int for each notification that must be defined
            notify(channelId++, builder.build())
        }

    }
}