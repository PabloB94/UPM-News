package es.upm.etsiinf.upmnews.utils.async;

import android.os.AsyncTask;
import android.util.Log;

import es.upm.etsiinf.upmnews.EditCreateForm;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class UploadArticleTask extends AsyncTask<Void, Void, Boolean> {

    //Article created to upload to the server
    private Article upload;
    //calling context of the app to perform callbacks
    private EditCreateForm context;

    //Constructor of the async task
    public UploadArticleTask(EditCreateForm con,Article ok){
        this.upload=ok;
        this.context=con;
    }

    //Async method executed
    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean res= true;
        try {
            ModelManager.saveArticle(upload);
        } catch (ServerCommunicationError e) {
            res=false;
            Log.i("UploadArticleTask",e.getMessage());
        }
        return res;
    }

    //Async method executed when task finished to display feedback of the operation
    @Override
    public void onPostExecute(Boolean res){
        if(res){
            context.saveOk();
        }else{
            context.saveFail();
        }
    }
    }
