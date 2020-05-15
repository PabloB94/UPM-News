package es.upm.etsiinf.upmnews.utils.async;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import es.upm.etsiinf.upmnews.AsyncResponse;
import es.upm.etsiinf.upmnews.MainActivity;
import es.upm.etsiinf.upmnews.R;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.AdaptadorListaArticulos;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;
public class LoadArticlesTask extends AsyncTask<Void, Void, List<Article>> {
    
	private static final String TAG = "LoadArticlesTask";



    public AsyncResponse delegate = null;
    private MainActivity context;
    public Boolean loggedin = false;

    public LoadArticlesTask(MainActivity context){
        this.context = context;
    }


    @Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;

        //If connection has been successful


        try {
            //AQUI HABRIA QUE EXTRAER DE LA STORED  EL USER Y LA PASSWORD Y COMPROBAR SI ES NULL O NO
            String strIdUser = "";
            String password = "";


           if (!ModelManager.isConnected()){
                SharedPreferences preferencia = context.getSharedPreferences("user_info",context.MODE_PRIVATE);
                strIdUser = preferencia.getString("id_user","");
                password = preferencia.getString("password","");
                Boolean guardado = !(password.equals("") || strIdUser.equals(""));
                if(guardado){
                    ModelManager.login(strIdUser, password);
                }
            }
            res = ModelManager.getArticles(6, 0);
//            for (Article article : res) {
//                // We print articles in Log
//                Log.i(TAG, article.toString());/////////////////////////////////añadido toString, daba error
//            }
        } catch (ServerCommunicationError e) {
            Log.e(TAG,e.getMessage());
        }
        catch (AuthenticationError e) {
            Log.e(TAG, e.getMessage());
        }

        return res;
    }

    @Override
    public void onPostExecute(List<Article> res){
        ListView listaArticulosView  = context.findViewById(R.id.listaArticulos);
        AdaptadorListaArticulos adaptador = new AdaptadorListaArticulos(context,res,loggedin);
        listaArticulosView.setAdapter(adaptador);
    }

}
