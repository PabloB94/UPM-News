package es.upm.etsiinf.upmnews;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Properties;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.RESTConnection;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;
public class LoadArticlesTask extends AsyncTask<Void, Void, List<Article>> {
    
	private static final String TAG = "LoadArticlesTask";

	//hardcoded stored credentials
    String strIdUser="";
    String strApiKey="";
    String strIdAuthUser="";
    //////////////////////////////////////////////////////////////////////////////////
    
	@Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;
		//ModelManager uses singleton pattern, connecting once per app execution in enough
        if (!ModelManager.isConnected()){
			// if it is the first login
            if (strIdUser==null || strIdUser.equals("")) {
                try {
                    ModelManager.login("ws_user", "ws_password");
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
				// obtain 6 articles from offset 0
                res = ModelManager.getArticles(6, 0);
                for (Article article : res) {
					// We print articles in Log
                    Log.i(TAG, article.toString());/////////////////////////////////añadido toString, daba error
                }
            } catch (ServerCommunicationError e) {
                Log.e(TAG,e.getMessage());
            }
        }
        return res;
    }
}
