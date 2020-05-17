package es.upm.etsiinf.upmnews.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;

    private final String DEFAULT_CHANNEL = "NEW ARTICLES";
    public static final String DEFAULT_CHANNEL_ID = "1";

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    //Create channel
    private void createChannels() {
        NotificationChannel defaultChannel =  new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        //Channel configuration
        defaultChannel.setShowBadge(true);
        defaultChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(defaultChannel);
    }

    //Create notification
    public void createNotification (int num) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Build the notification
        Notification.Builder mBuilder= new Notification.Builder(getApplicationContext(), DEFAULT_CHANNEL_ID)
                .setContentTitle("UPM_NEWS")
                .setContentText( num +" New articles available on UPM NEWS")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_news)
                .setAutoCancel(true);
        getManager().notify(1,mBuilder.build());

    }

    //Obtain the date when an article is created or modified
    public String getDate(){
        SharedPreferences preferencia = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String res=preferencia.getString("date",SerializationUtils.dateToString(null));
        return res;
    }

    //Gets an instance of the NotificationManager service
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
