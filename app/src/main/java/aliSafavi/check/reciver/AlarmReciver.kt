package aliSafavi.check.reciver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Toast.makeText(context,"ssss",Toast.LENGTH_SHORT).show()
        deliverNotification(context)
    }

    private fun deliverNotification(context: Context) {
//        val intent
    }
}