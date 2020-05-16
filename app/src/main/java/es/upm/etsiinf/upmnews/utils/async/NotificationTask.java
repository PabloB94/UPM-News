package es.upm.etsiinf.upmnews.utils.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.NotificationJobService;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class NotificationTask extends AsyncTask<Void, Void, List<Article>> {

    private NotificationJobService context;
    private String checkDate;
    public NotificationTask(NotificationJobService con, String check){
        this.context = con;
        this.checkDate=check;
    }
    @Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;

        try {
            //llamada de model manager para buscar nuevos articulos
            res= ModelManager.getUpdates(checkDate);
            Log.w("NotificationTask", "New Articles: "+ res.size());
        } catch (ServerCommunicationError e) {
            Log.e("NotificationTask",e.getMessage());
        }

        return res;
    }

    @Override
    public void onPostExecute(List<Article> res){
        if(res!=null){
            context.generateNotifications(res.size());
        }
    }
}
