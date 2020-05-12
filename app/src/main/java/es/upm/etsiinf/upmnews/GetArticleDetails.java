package es.upm.etsiinf.upmnews;

import android.os.AsyncTask;
import android.util.Log;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

import static es.upm.etsiinf.upmnews.utils.Logger.TAG;

public class GetArticleDetails extends AsyncTask<Void, Void, Article> {

    //hardcoded stored credentials
    String strIdUser="";
    String strApiKey="";
    String strIdAuthUser="";

    public AsyncResponse delegate = null;
    private DetailsScreen context;
    private String id;

    public GetArticleDetails(DetailsScreen cont,String id_art){
        this.context=cont;
        this.id=id_art;
    }

    @Override
    protected Article doInBackground(Void... voids) {
        Article res = null;
        //ModelManager uses singleton pattern, connecting once per app execution in enough
        if (!ModelManager.isConnected()){
            // if it is the first login
            if (strIdUser==null || strIdUser.equals("")) {
                try {
                    ModelManager.login("DEV_TEAM_02", "01394");
                    strApiKey = ModelManager.getLoggedApiKey();
                    strIdAuthUser = ModelManager.getLoggedAuthType();
                    strIdUser = ModelManager.getLoggedIdUSer();
                } catch (AuthenticationError e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            // if we have saved user credentials from previous connections
            else{
                ModelManager.stayloggedin(strIdUser,strApiKey,strIdAuthUser);
            }
        }
        //If connection has been successful
        if (ModelManager.isConnected()) {
            try {
                // obtain article by id
                res =  ModelManager.getArticle(Integer.parseInt(id));
                    Log.i(TAG, res.toString());
            } catch (ServerCommunicationError e) {
                Log.e(TAG,e.getMessage());
            }
        }
        return res;
    }

    @Override
    public void onPostExecute(Article res){
            context.loadElements(res);
            //porque no mejor en el onclicklistener le pasamos un articulo de la lista
            //mainactivity para que sirve el processfinish
            //porque implementa asyncresponse sino lo usa
    }
}
