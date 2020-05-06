package es.upm.etsiinf.upmnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.AdaptadorListaArticulos;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;

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


        Button b = findViewById(R.id.btest);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent testIntent = new Intent(MainActivity.this,DetailsScreen.class);
                startActivity(testIntent);
            }
        });

    }

    @Override
    public void processFinish(List<Article> output) {
       /* int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Hola";
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();*/
    }


}
