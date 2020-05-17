package es.upm.etsiinf.upmnews.utils.async;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

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
    public List<Article> listaArticulos;
    public AdaptadorListaArticulos adaptador;
    Parcelable state = null;
    ListView listaArticulosView ;

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
            String strApiKey = "";
            String strIdAuthUser = "";


           if (!ModelManager.isConnected()){
                SharedPreferences preferencia = context.getSharedPreferences("user_info",context.MODE_PRIVATE);
                strIdUser = preferencia.getString("id_user","");
               strApiKey = preferencia.getString("strApiKey","");
               strIdAuthUser = preferencia.getString("strIdAuthUser","");
                Boolean guardado = !(strApiKey.equals("") || strIdUser.equals("") || strIdAuthUser.equals(""));
                    if(guardado){
                    ModelManager.stayloggedin(strIdUser,strApiKey,strIdAuthUser);
                    loggedin = true;
                }
            }
           else{
               loggedin = true;
           }
            res = ModelManager.getArticles(30, context.offsetL);

        } catch (ServerCommunicationError e) {
            Log.e(TAG,e.getMessage());
        }

        return res;
    }

    @Override
    public void onPostExecute(List<Article> res){
        listaArticulosView  = context.findViewById(R.id.listaArticulos);
        Boolean scroll = false;
        int tamanioAntiguo = 0;

        if(!res.isEmpty()){

        if(listaArticulos != null){
            tamanioAntiguo = listaArticulos.size();
            listaArticulos.addAll(res);
            res = listaArticulos;
        }
        else{
            listaArticulos = res;
            res = listaArticulos;
        }

        adaptador = new AdaptadorListaArticulos(context,res,loggedin);
        adaptador.filter(context.getTopic());
        listaArticulosView.setAdapter(adaptador);

        if(tamanioAntiguo != 0 && tamanioAntiguo != listaArticulos.size()){
            context.offsetL = res.size();
            listaArticulosView.onRestoreInstanceState(state);
        }

        if(loggedin){
            context.findViewById(R.id.loginButton).setVisibility(View.GONE);
            context.findViewById(R.id.newArticleButton).setVisibility(View.VISIBLE);
        }
        listaArticulosView.setOnScrollListener(new AbsListView.OnScrollListener(){
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(lastFirstVisibleItem<firstVisibleItem){
                    if(firstVisibleItem + visibleItemCount >= totalItemCount){
                        //context.offsetL = firstVisibleItem + visibleItemCount;

                        LoadArticlesTask task = new LoadArticlesTask(context);
                        task.delegate = context;
                        task.listaArticulos = listaArticulos;
                        task.state = listaArticulosView.onSaveInstanceState();
                        task.execute();

                    }
                }
                if(lastFirstVisibleItem> firstVisibleItem){
                    if(loggedin) context.findViewById(R.id.newArticleButton).setVisibility(View.VISIBLE);
                }
                lastFirstVisibleItem=firstVisibleItem;
            }
        });
        context.setArticles(res);
        }
        else{
            if(loggedin) context.findViewById(R.id.newArticleButton).setVisibility(View.GONE);
        }


    }


}
