package impros.cale.yksgerisayim.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import impros.cale.yksgerisayim.Helpers.UI;
import impros.cale.yksgerisayim.MainActivity;
import impros.cale.yksgerisayim.R;


public class AlarmReceiver extends BroadcastReceiver {
    int MID=100;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            UI.log("Bildirim Geldi");

            NotificationChannel notificationChannel = new NotificationChannel("1", "YKSSAYACI", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Sayac Kanalı");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            // notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            // notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Merhaba :)")
                .setContentText("YKS'ye kaç gün kaldığını görmek için buraya dokunabilirsin :) ").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent);
               // .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(100, mNotifyBuilder.build());
        MID++;

    }
}