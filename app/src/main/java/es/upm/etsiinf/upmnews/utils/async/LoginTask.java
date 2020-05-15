package es.upm.etsiinf.upmnews.utils.async;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import es.upm.etsiinf.upmnews.AsyncResponse;
import es.upm.etsiinf.upmnews.R;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;


public class LoginTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "LoadArticlesTask";

    private String strIdUser;
    private String strApiKey;
    private String strIdAuthUser;
    private String password;
    private Boolean guardar;


    public AsyncResponse delegate = null;
    private Context context;
    private Dialog myDialog;


    public LoginTask(String user, String pwd, Dialog myDialog, Context context, Boolean guardar){
        this.strIdUser = user;
        this.password = pwd;
        this.myDialog = myDialog;
        this.context = context;
        this.guardar = guardar;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            //Login
            ModelManager.login(strIdUser, password);
            strApiKey = ModelManager.getLoggedApiKey();
            strIdAuthUser = ModelManager.getLoggedAuthType();

            //Guardar información del usuario
            if(guardar && (strApiKey != "" || strApiKey != null)){
                SharedPreferences preferencia = context.getSharedPreferences("user_info",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencia.edit();
                editor.putString("id_user", strIdUser);
                editor.putString("password", password);
                editor.commit();
            }

            return true;

        } catch (AuthenticationError e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
    //If connection has been successful


    @Override
    public void onPostExecute(Boolean success){
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        if(success){
            text  = context.getString(R.string.login_success);
        }else{
            text = context.getString(R.string.login_error);
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        myDialog.cancel();
        delegate.processFinish(success);
    }

}
