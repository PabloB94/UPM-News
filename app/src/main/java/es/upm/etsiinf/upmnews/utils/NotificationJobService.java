package es.upm.etsiinf.upmnews.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;
import es.upm.etsiinf.upmnews.utils.async.NotificationTask;

public class NotificationJobService extends JobService {
    private JobParameters params;
    private NotificationTask task;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        params = jobParameters;
        Calendar calen= Calendar.getInstance();
        calen.add(Calendar.MINUTE, -15);
        String date = SerializationUtils.dateToString(calen.getTime());//mirar que fecha enviar a la llamada
        task = new NotificationTask(this, date);
        task.execute();
        Log.w("NotificationJobService","Initiated");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(task!=null){
        task.cancel(true);
        }
        Log.e("NotificationJobService","Stopped without finish due to requirements");
        return false;
    }

    public void generateNotifications(int num){//numero de articulos nuevos

        if(num>0){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                    .setContentTitle("UPM_NEWS")
                    .setContentText("New articles available on UPM NEWS")
                    .setGroup("All")
                    .setGroupSummary(true)
                    .setSmallIcon(R.drawable.ic_news)
                    .setAutoCancel(true);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(1,mBuilder.build());
        }
        jobFinished(params,true);
    }
}

