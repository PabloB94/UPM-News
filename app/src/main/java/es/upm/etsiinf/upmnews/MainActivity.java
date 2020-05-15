package es.upm.etsiinf.upmnews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    Bundle savedInstanceState;
    Context context = this;
    Boolean guardar = false;
    MainActivity main = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        Properties prop = new Properties();
        prop.setProperty("service_url","https://sanger.dia.fi.upm.es/pmd-task/");
        prop.setProperty("require_self_signed_cert","TRUE");
        ModelManager.configureConnection(prop);
        LoadArticlesTask task = new LoadArticlesTask(this);
        task.delegate = this;
        task.execute();


    }

    @Override
    public void processFinish(Boolean loginSuccess) {
        if(loginSuccess){
            LoadArticlesTask task = new LoadArticlesTask(this);
            task.delegate = this;
            task.loggedin = true;
            task.execute();
            this.findViewById(R.id.loginButton).setVisibility(View.GONE);
            this.findViewById(R.id.newArticleButton).setVisibility(View.VISIBLE);
        }
    }

    public void callLoginDialog(View view)
    {
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.login_popup);
        myDialog.setCancelable(false);
        Button login = myDialog.findViewById(R.id.loginDialogButton);
        Button cancel = myDialog.findViewById(R.id.cancelDialogButton);
        Switch switchGuardar = myDialog.findViewById(R.id.switchRemember);
        switchGuardar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    guardar = isChecked;
            }
        });
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

                LoginTask task = new LoginTask(user, pwd, myDialog, context, guardar);
                task.delegate = main;
                task.execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v){
                myDialog.cancel();
            }
        });
    }

    public void newArticle(View view) {

    }
}
