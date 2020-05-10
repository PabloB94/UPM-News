package es.upm.etsiinf.upmnews;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Properties;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.AdaptadorListaArticulos;
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


    public AsyncResponse delegate = null;
    private MainActivity context;
    public LoadArticlesTask(MainActivity context){
        this.context = context;
    }


    @Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;
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

    @Override
    public void onPostExecute(List<Article> res){
        ListView listaArticulosView  = context.findViewById(R.id.listaArticulos);
        AdaptadorListaArticulos adaptador = new AdaptadorListaArticulos(context,res);
        listaArticulosView.setAdapter(adaptador);
        delegate.processFinish(res);
    }

}
