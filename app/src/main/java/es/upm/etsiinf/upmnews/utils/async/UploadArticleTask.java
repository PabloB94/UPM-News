package es.upm.etsiinf.upmnews.utils.async;

import android.os.AsyncTask;
import android.util.Log;

import es.upm.etsiinf.upmnews.EditCreateForm;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class UploadArticleTask extends AsyncTask<Void, Void, Boolean> {

    private Article upload;
    private EditCreateForm context;

    public UploadArticleTask(EditCreateForm con,Article ok){
        this.upload=ok;
        this.context=con;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean res= true;
        try {
            int id = ModelManager.saveArticle(upload);
        } catch (ServerCommunicationError e) {
            res=false;
            Log.i("UploadArticleTask",e.getMessage());
        }
        return res;
    }
    @Override
    public void onPostExecute(Boolean res){
        if(res){
            context.saveOk();
        }else{
            context.saveFail();
        }
    }
    }
