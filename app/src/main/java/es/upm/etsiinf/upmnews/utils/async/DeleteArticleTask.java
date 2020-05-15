package es.upm.etsiinf.upmnews.utils.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import es.upm.etsiinf.upmnews.AsyncResponse;
import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class DeleteArticleTask extends AsyncTask<Void, Void, Boolean> {

    private String id;
    private Context context;
    public AsyncResponse delegate = null;

    public DeleteArticleTask(String id, Context context){
        this.id = id;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean res= true;
        try {
            ModelManager.deleteArticle(Integer.parseInt(id));
        } catch (ServerCommunicationError e) {
            res=false;
            Log.i("DeleteArticleTask",e.getMessage());
        }
        return res;
    }
    @Override
    public void onPostExecute(Boolean res){
        String text;
        if(res){
            text = context.getString(R.string.delete_success);
        }else{
            text = context.getString(R.string.delete_error);
        }
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        MainActivity ma = (MainActivity) context;
        ma.refresh();
    }

}
