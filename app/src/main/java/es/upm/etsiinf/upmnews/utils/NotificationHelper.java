package es.upm.etsiinf.upmnews.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public String savedDate="";
    private final String DEFAULT_CHANNEL = "NEW ARTICLES";
    public static final String DEFAULT_CHANNEL_ID = "1";

    private final String GROUP_NAME = "All";
    public static final int GROUP_ID = 1;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        NotificationChannel defaultChannel =  new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        defaultChannel.setShowBadge(true);
        defaultChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(defaultChannel);
    }

    public void createNotification (int num) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder= new Notification.Builder(getApplicationContext(), DEFAULT_CHANNEL_ID)
                .setContentTitle("UPM_NEWS")
                .setContentText( num +" New articles available on UPM NEWS")
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_NAME)
                .setGroupSummary(true)
                .setSmallIcon(R.drawable.ic_news)
                .setAutoCancel(true);
        getManager().notify(GROUP_ID,mBuilder.build());

    }

    public String getDate(){
        SharedPreferences preferencia = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String res=preferencia.getString("date",SerializationUtils.dateToString(null));
        Log.w("Notification Date",res);
        return res;
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
