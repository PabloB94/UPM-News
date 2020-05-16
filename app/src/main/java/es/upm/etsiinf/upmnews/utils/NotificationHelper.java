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
    private final String DEFAULT_CHANNEL = "NEW ARTICLES";
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

    public Notification.Builder createNotification (String channelID,String category) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), channelID)
                .setContentTitle("UPM_NEWS")
                .setContentText("New articles available on UPM NEWS")
                .setContentIntent(pendingIntent)
                //.setStyle(new Notification.BigTextStyle().bigText(msg))
                .setGroup(category)
                .setSmallIcon(R.drawable.ic_news)
                .setAutoCancel(true);

    }

    public void publishNotificationGroup (String category) {
        String channelID = DEFAULT_CHANNEL_ID;

        Notification groupNotification = new Notification.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_news)
                .setGroup(category)
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

    ///////////////////////// ESTO CREO DE AQUI ABAJO ES LO QUE REALMENTE HAY QUE HACER PERO
    //////////////////////// NO ESTOY SEGURO
    /*public class MainActivity extends AppCompatActivity {
        NotificationCompat.Builder mBuilder;
        private final String DEFAULT_CHANNEL = "DEFAULT_CHANNEL";
        public static final String DEFAULT_CHANNEL_ID = "1";


        @Override
        protected void onCreate(Bundle savedIntanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.Layout.activity_main);


            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_news)
                    .setContentTitle("UPM_NEWS")
            //.setContextText("Tienes 1 mensaje")

            // Activity que se lanza al hacer click la notificación
            Intent resultIntent = new Intent(this, ResultActivity.class); // ResultActivity como ejemplo

            // no need to create an artificial back state
            PendindIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
.
            mBuilder.setContentIntent(resultPendingIntent);

            NotificacionManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(DEFAULT_CHANNEL_ID, mBuilder.build());
        }
    } */
}
