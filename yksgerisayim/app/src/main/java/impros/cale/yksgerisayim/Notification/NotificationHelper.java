package impros.cale.yksgerisayim.Notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;


public class NotificationHelper {
    public static void createNotification(Activity act, int hour, int minute, int bildirimAralik) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Intent intent1 = new Intent(act, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(act, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) act.getSystemService(act.ALARM_SERVICE);
        long bildirimGun = bildirimAralik * (1000 * 60 * 60 * 24);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), bildirimGun, pendingIntent);
    }
}
