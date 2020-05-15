package es.upm.etsiinf.upmnews.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    private final String DEFAULT_CHANNEL = "DEFAULT_CHANNEL";
    public static final String DEFAULT_CHANNEL_ID = "1";

    private final String GROUP_NAME = "GROUP";
    public static final int GROUP_ID = 1;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel defaultChannel =  new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            defaultChannel.setShowBadge(true);
            defaultChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);

            getManager().createNotificationChannel(defaultChannel);
        }
    }

    private Notification.Builder createNotification (String channelID) {
        Intent intent = new Intent(this, Activity.class); // No se que actividad es
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //intent.putExtra("msg",msg);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), channelID)
                .setContentTitle("UPM_NEWS")
                .setContentText("Notificación del articulo nuevo en el server o modificado")
                .setContentIntent(pendingIntent)
                //.setStyle(new Notification.BigTextStyle().bigText(msg))
                .setGroup(GROUP_NAME)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

    }

    public void publishNotificationGroup (boolean priority) {
        String channelID = DEFAULT_CHANNEL_ID;

        Notification groupNotification = new Notification.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_news)
                .setGroup(GROUP_NAME)
                .setGroupSummary(true)
                .build();
        getManager().notify(GROUP_ID, groupNotification);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
