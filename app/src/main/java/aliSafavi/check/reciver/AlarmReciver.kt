package aliSafavi.check.reciver

import aliSafavi.check.MainActivity.Companion.CHANNEL_ID
import aliSafavi.check.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat

class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val id = intent.extras?.get("checkId")

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(System.currentTimeMillis().toString())
            .setContentText(id.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(VISIBILITY_PUBLIC)
            .setNumber(4)
            .setTicker("big 1")

//        NotificationManagerCompat.from(context)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun deliverNotification(context: Context, pendingIntent: PendingIntent) {


    }
}