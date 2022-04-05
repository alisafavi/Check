package aliSafavi.check.reciver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
//        val intent
    }
}