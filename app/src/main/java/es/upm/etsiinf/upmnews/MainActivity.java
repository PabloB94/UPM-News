package es.upm.etsiinf.upmnews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    Bundle savedInstanceState;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
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
    public void processFinish(List<Article> output) {}

    public void callLoginDialog(View view)
    {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.login_popup);
        myDialog.setCancelable(false);
        Button login = myDialog.findViewById(R.id.loginDialogButton);
        Button cancel = myDialog.findViewById(R.id.cancelDialogButton);

        final EditText username = myDialog.findViewById(R.id.et_username);
        final EditText password = myDialog.findViewById(R.id.et_password);
        myDialog.show();

        login.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String user = username.getText().toString();
                String pwd = password.getText().toString();
                LoginTask task = new LoginTask(user, pwd, myDialog, context);
                task.execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                myDialog.cancel();
                Intent testIntent = new Intent(MainActivity.this,EditCreateForm.class);
                testIntent.putExtra("id","1250");
                startActivity(testIntent);
            }
        });
    }
}
