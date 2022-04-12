package aliSafavi.check.reciver

import aliSafavi.check.MainActivity
import aliSafavi.check.MainActivity.Companion.CHANNEL_ID
import aliSafavi.check.R
import aliSafavi.check.bank.BankListFragment
import aliSafavi.check.check.CheckFragment
import aliSafavi.check.check_list.CheckListFragment
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.navDeepLink
import com.xdev.arch.persiancalendar.datepicker.calendar.PersianCalendar

class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val name = intent.extras?.getString("name")
        val amount = intent.extras?.getString("amount")
        val dateInML = intent.extras?.getLong("date")

        val date = PersianCalendar(dateInML!!).run {
            month++
            toString()
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.extras?.let { putExtras(it) }
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_IMMUTABLE)


        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(name)
            .setContentText("\$ ${amount} \t ${date} ")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(VISIBILITY_PUBLIC)


//        NotificationManagerCompat.from(context)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}