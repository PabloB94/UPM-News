package es.upm.etsiinf.upmnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    ListView listaArticulosView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ModelManager mm = new ModelManager();
        Properties prop = new Properties();
        prop.setProperty("service_url","https://sanger.dia.fi.upm.es/pmd-task/");
        prop.setProperty("require_self_signed_cert","TRUE");
        ModelManager.configureConnection(prop);
        LoadArticlesTask task = new LoadArticlesTask(this);
        task.delegate = this;
        task.execute();

    }

    @Override
    public void processFinish(List<Article> output) {
       /* int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Hola";
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();*/
    }


}
