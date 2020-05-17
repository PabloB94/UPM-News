package es.upm.etsiinf.upmnews.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import es.upm.etsiinf.upmnews.utils.async.NotificationTask;

public class NotificationJobService extends JobService {
    private JobParameters params;
    private NotificationTask task;
    private NotificationHelper noti;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        params = jobParameters;
        noti = new NotificationHelper(this);
        String date = noti.getDate();
        task = new NotificationTask(this, date);
        task.execute();
        Log.w("NotificationJobService","Initiated with date" + date);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(task!=null){
        task.cancel(true);
        }
        Log.e("NotificationJobService","Stopped without finish due to requirements or initialization of classes");
        return false;
    }

    public void generateNotifications(int num){//numero de articulos nuevos

        if(num>0){
           noti.createNotification(num);
        }
        jobFinished(params,true);
    }
}

