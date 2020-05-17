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
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;
public class LoadArticlesTask extends AsyncTask<Void, Void, List<Article>> {
    
	private static final String TAG = "LoadArticlesTask";

	//calling contexts of the app to perform callbacks
    public AsyncResponse delegate = null;
    private MainActivity context;
    //variable to control visibility of special buttons
    public Boolean loggedin = false;
    //Local list of articles
    public List<Article> listaArticulos;
    //adapter of the Articles List
    public AdaptadorListaArticulos adaptador;
    //position during scroll to load more articles when end is reached
    Parcelable state = null;
    //reference to the Articles List of the mainScreen
    ListView listaArticulosView ;

    //Constructor of the async task
    public LoadArticlesTask(MainActivity context){
        this.context = context;
    }


    //Async method executed to perform the login
    @Override
    protected List<Article> doInBackground(Void... voids) {
        List<Article> res = null;

        try {
            String strIdUser;
            String strApiKey ;
            String strIdAuthUser;

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

    //Async method executed when task finished, loading the list of articles into the mainScreen with the scroller listener and configuring the list adapter
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
        //Scroll Listener to load more Articles
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
