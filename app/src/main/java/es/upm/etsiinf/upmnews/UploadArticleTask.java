package es.upm.etsiinf.upmnews;

import android.os.AsyncTask;
import android.util.Log;

import es.upm.etsiinf.upmnews.EditCreateForm;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class UploadArticleTask extends AsyncTask<Void, Void, Integer> {
    
    private Article upload;
    private EditCreateForm context;

    public UploadArticleTask(EditCreateForm con,Article ok){
        this.upload=ok;
        this.context=con;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int res=-1;
        try {
            res = ModelManager.saveArticle(upload);
        } catch (ServerCommunicationError e) {
            Log.i("EditCreateForm",e.getMessage());
        }
        return res;
    }
    @Override
    public void onPostExecute(int res){

    }
    }
