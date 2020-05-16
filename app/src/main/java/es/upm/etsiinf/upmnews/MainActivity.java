package es.upm.etsiinf.upmnews;

import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Properties;

import androidx.appcompat.app.AppCompatActivity;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.NotificationHelper;
import es.upm.etsiinf.upmnews.utils.NotificationJobService;
import es.upm.etsiinf.upmnews.utils.async.LoadArticlesTask;
import es.upm.etsiinf.upmnews.utils.async.LoginTask;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    Bundle savedInstanceState;
    Context context = this;
    Boolean guardar = false;
    MainActivity main = this;
    public int offsetL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        Properties prop = new Properties();
        prop.setProperty("service_url","https://sanger.dia.fi.upm.es/pmd-task/");
        prop.setProperty("require_self_signed_cert","TRUE");
        ModelManager.configureConnection(prop);
        refresh();
        scheduleNotifications();

    }

    private void scheduleNotifications(){
        ComponentName serviceComponent = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1*60000);
        builder.setOverrideDeadline(2*60000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
        Log.w("Notifications ","Created");
        //Configure the notifications
        NotificationHelper help = new NotificationHelper(this);

    }

    @Override
    public void processFinish(Boolean loginSuccess) {
        if(loginSuccess){
            refresh();
            this.findViewById(R.id.loginButton).setVisibility(View.GONE);
            this.findViewById(R.id.newArticleButton).setVisibility(View.VISIBLE);
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

    }
}
