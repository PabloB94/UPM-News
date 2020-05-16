package es.upm.etsiinf.upmnews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.async.DeleteArticleTask;
import es.upm.etsiinf.upmnews.utils.async.LoadArticlesTask;
import es.upm.etsiinf.upmnews.utils.async.LoginTask;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    Bundle savedInstanceState;
    Context context = this;
    Boolean guardar = false;
    MainActivity main = this;
    public int offsetL = 0;
    Menu barMenu;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);
        Properties prop = new Properties();
        prop.setProperty("service_url","https://sanger.dia.fi.upm.es/pmd-task/");
        prop.setProperty("require_self_signed_cert","TRUE");
        ModelManager.configureConnection(prop);

    }

    @Override
    public void processFinish(Boolean loginSuccess) {
        if(loginSuccess){
            refresh();
            this.findViewById(R.id.loginButton).setVisibility(GONE);
            this.findViewById(R.id.newArticleButton).setVisibility(VISIBLE);
        }
    }

    @Override
    public void processData(Article output) {
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

    public void newArticle(View v) {
        Intent editArticle = new Intent(context, EditCreateForm.class);
        editArticle.putExtra( "id", "-1");
        context.startActivity(editArticle);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    public void refresh() {
        LoadArticlesTask task = new LoadArticlesTask(this);
        task.delegate = this;
        task.loggedin = ModelManager.isConnected();
        this.offsetL = 0;
        task.execute();
        MenuItem logout = null;
        if (menu != null) logout = menu.findItem(R.id.logout);
        if (ModelManager.isConnected()) {
            this.findViewById(R.id.loginButton).setVisibility(GONE);
            this.findViewById(R.id.newArticleButton).setVisibility(VISIBLE);
            if (logout != null) logout.setVisible(true);
        }else{
            this.findViewById(R.id.newArticleButton).setVisibility(GONE);
            this.findViewById(R.id.loginButton).setVisibility(VISIBLE);
            if (logout != null) logout.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;

            case R.id.filter:
                Toast toast2 = Toast.makeText(this, "Filters", Toast.LENGTH_SHORT);
                toast2.show();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private void logout(){
        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(context);
        logoutDialog.setTitle(R.string.confirm_logout_title);
        logoutDialog.setMessage(R.string.logout_confirm);
        logoutDialog.setIcon(R.drawable.logout_black);
        logoutDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(context, R.string.loging_out, Toast.LENGTH_SHORT);
                        toast.show();
                        ModelManager.logout(context);
                        refresh();
                    }
                });
        logoutDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        logoutDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        this.menu = menu;
        menu.findItem(R.id.logout).setVisible(ModelManager.isConnected());
        refresh();
        return true;
    }

}
