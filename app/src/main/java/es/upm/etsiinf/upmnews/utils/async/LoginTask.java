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

    //credentials to perform the login connection to the server
    private String strIdUser;
    private String strApiKey;
    private String strIdAuthUser;
    private String password;
    //variable to control the remember me option
    private Boolean guardar;
    //calling contexts of the app to perform callbacks
    public AsyncResponse delegate = null;
    private Context context;
    private Dialog myDialog;


    //Constructor of the async task
    public LoginTask(String user, String pwd, Dialog myDialog, Context context, Boolean guardar){
        this.strIdUser = user;
        this.password = pwd;
        this.myDialog = myDialog;
        this.context = context;
        this.guardar = guardar;
    }

    //Async method executed
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            //Login
            ModelManager.login(strIdUser, password);
            strApiKey = ModelManager.getLoggedApiKey();
            strIdAuthUser = ModelManager.getLoggedAuthType();
            strIdUser = ModelManager.getLoggedIdUSer();

            //Guardar información del usuario
            if(guardar && (!strApiKey.equals("") || strApiKey != null)){
                SharedPreferences preferencia = context.getSharedPreferences("user_info",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencia.edit();
                editor.putString("id_user", strIdUser);
                editor.putString("strApiKey", strApiKey);
                editor.putString("strIdAuthUser", strIdAuthUser);
                editor.commit();
            }

            return true;

        } catch (AuthenticationError e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    //Async method executed when task finished to display feedback of the operation
    @Override
    public void onPostExecute(Boolean success){
        CharSequence text;
        if(success){
            text  = context.getString(R.string.login_success);
        }else{
            text = context.getString(R.string.login_error);
        }
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
        myDialog.cancel();
        delegate.processFinish(success);
    }

}
