package es.upm.etsiinf.upmnews.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import es.upm.etsiinf.upmnews.utils.async.NotificationTask;

public class NotificationJobService extends JobService {
    private JobParameters params;
    //Async task
    private NotificationTask task;
    //Variable to configure the notifications
    private NotificationHelper noti;

    //Job executed by the service
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        params = jobParameters;
        noti = new NotificationHelper(this);
        String date = noti.getDate();
        task = new NotificationTask(this, date);
        task.execute();
        return true;
    }

    //Job executed when service stop abruptly
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(task!=null){
        task.cancel(true);
        }
        Log.e("NotificationJobService","Stopped without finish due to requirements or initialization of classes");
        return false;
    }
    //Method to generate Notifications
    public void generateNotifications(int num){
        if(num>0){
           noti.createNotification(num);
        }
        jobFinished(params,true);
    }
}