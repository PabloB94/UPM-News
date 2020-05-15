package es.upm.etsiinf.upmnews;

import android.os.AsyncTask;
import android.util.Log;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

import static es.upm.etsiinf.upmnews.utils.Logger.TAG;

public class GetArticleDetails extends AsyncTask<Void, Void, Article> {

    private AsyncResponse context;
    private String id;

    GetArticleDetails(AsyncResponse cont, String id_art){
        this.context = cont;
        this.id = id_art;
    }

    @Override
    protected Article doInBackground(Void... voids) {
        Article res = null;
            try {
                // obtain article by id
                res =  ModelManager.getArticle(Integer.parseInt(id));
                    Log.i(TAG, res.toString());
            } catch (ServerCommunicationError e) {
                Log.e(TAG,e.getMessage());
            }
        return res;
    }

    @Override
    public void onPostExecute(Article res){ context.processData(res);}
}
