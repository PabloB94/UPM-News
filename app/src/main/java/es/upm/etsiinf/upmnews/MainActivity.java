package es.upm.etsiinf.upmnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Properties;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.btest);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent testIntent = new Intent(MainActivity.this,DetailsScreen.class);
                startActivity(testIntent);
            }
        });

        Button connButton = findViewById(R.id.connButton);
        connButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ModelManager mm = new ModelManager();
                Properties prop = new Properties();
                prop.setProperty("service_url","https://sanger.dia.fi.upm.es/pmd-task/");
                prop.setProperty("require_self_signed_cert","TRUE");
                ModelManager.configureConnection(prop);
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;


                try {
                    ModelManager.login("DEV_TEAM_02","01394");
                    CharSequence text = "Conectado!";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (AuthenticationError authenticationError) {
                    authenticationError.printStackTrace();
                    CharSequence text = "Error";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                //Intent testIntent = new Intent(MainActivity.this,DetailsScreen.class);
                //startActivity(testIntent);
            }
        });
    }
}
