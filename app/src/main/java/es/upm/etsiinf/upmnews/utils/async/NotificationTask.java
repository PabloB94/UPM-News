package es.upm.etsiinf.upmnews.utils.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.NotificationJobService;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class NotificationTask extends AsyncTask<Void, Void, List<Article>> {

    //calling context of the app to perform callbacks
    private NotificationJobService context;
    //local field with the date of the last article
    private String checkDate;

    //Constructor of the async task
    public NotificationTask(NotificationJobService con, String check){
        this.context = con;
        this.checkDate=check;
    }

    //Async method executed
    @Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;

        try {
            res= ModelManager.getUpdates(checkDate);
        } catch (ServerCommunicationError e) {
            Log.e("NotificationTask",e.getMessage());
        }

        return res;
    }

    //Async method executed when task finished generating notifications
    @Override
    public void onPostExecute(List<Article> res){
        if(res!=null){
            context.generateNotifications(res.size());
        }
    }
}
