package es.upm.etsiinf.upmnews.utils.network;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.simple.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.Logger;
import es.upm.etsiinf.upmnews.utils.network.exceptions.AuthenticationError;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

import static es.upm.etsiinf.upmnews.utils.network.ServiceCallUtils.parseHttpStreamResult;

public class ModelManager {

    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENTTYPE = "Content-Type";
    private static final String CHARSET = "charset";
    private static final String URLENCODED = "application/x-www-form-urlencoded";
    private static final String UTF8 = "utf-8";
    private static final String CONTENTLENGTH = "Content-Length";


    private ModelManager(){
        throw new IllegalStateException("Utility Class");
    }

    private static RESTConnection rc = null;

    public static boolean isConnected(){
        return rc.idUser!=null;
    }

    public static String getLoggedIdUSer(){
        return rc.idUser;
    }

    public static String getLoggedApiKey(){
        return rc.apikey;
    }

    public static String getLoggedAuthType(){
        return rc.authType;
    }

    /**
     *
     * @param ini Initializes entity manager urls and users
     * @throws AuthenticationError
     */
    public static void configureConnection(Properties ini)  {
        rc = new RESTConnection(ini);
    }

    public static void stayloggedin(String idUser, String apikey, String authType) {
        rc.idUser = idUser;
        rc.authType = authType;
        rc.apikey = apikey;
    }

        /**
         * Login onto remote service
         * @param username
         * @param password
         * @throws AuthenticationError
         */
    @SuppressWarnings("unchecked")
    public static void login(String username, String password) throws AuthenticationError{
        String res = "";
        try{
            String request = rc.serviceUrl + "login";

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(CONTENTTYPE, "application/json");
            connection.setRequestProperty(CHARSET, UTF8);
            connection.setUseCaches (false);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("passwd", password);

            ServiceCallUtils.writeJSONParams(connection, jsonParam);

            int httpResult =connection.getResponseCode();
            if(httpResult ==HttpURLConnection.HTTP_OK){
                res = parseHttpStreamResult(connection);

                JSONObject userJsonObject = ServiceCallUtils.readRestResultFromSingle(res);
                rc.idUser = userJsonObject.get("user").toString();
                rc.authType = userJsonObject.get(AUTHORIZATION).toString();
                rc.apikey = userJsonObject.get("apikey").toString();
                rc.isAdministrator = userJsonObject.containsKey("administrator");
            }else{
                Logger.log(Logger.ERROR,connection.getResponseMessage());

                throw new AuthenticationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            throw new AuthenticationError(e.getMessage());
        }
    }

    /**
     *
     * @return user id logged in
     */
    public static String getIdUser(){
        return rc.idUser;
    }

    /**
     *
     * @return auth token header for user logged in
     */
    private static String getAuthTokenHeader(){
        return rc.authType + " apikey=" + rc.apikey;
    }

    /**
     *
     * @return the list of articles in remote service
     * @throws ServerCommunicationError
     */
    public static List<Article> getArticles() throws ServerCommunicationError{
        return getArticles(-1,-1);
    }

    /**
     *
     * @return the list of articles in remote service with pagination
     * @throws ServerCommunicationError
     */
    public static List<Article> getArticles(int buffer, int offset) throws ServerCommunicationError{
        String limits = "";
        if (buffer>0 && offset >=0){
            limits = "/"+buffer+"/"+offset;
        }

        List<Article> result = new ArrayList<>();
        try{
            String parameters =  "";
            String request = rc.serviceUrl + "articles" + limits;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned) TrustModifier.relaxHostChecking(connection);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty(CONTENTTYPE, URLENCODED);
            connection.setRequestProperty(AUTHORIZATION, getAuthTokenHeader());
            connection.setRequestProperty(CHARSET, UTF8);
            connection.setRequestProperty(CONTENTLENGTH, "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches (false);

            int httpResult =connection.getResponseCode();
            if(httpResult ==HttpURLConnection.HTTP_OK){
                String res = parseHttpStreamResult(connection);
                List<JSONObject> objects = ServiceCallUtils.readRestResultFromList(res);
                for (JSONObject jsonObject : objects) {
                    result.add(new Article(jsonObject));
                }
                Logger.log (Logger.INFO, objects.size() + " objects (Article) retrieved");
            }else{
                throw new ServerCommunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log (Logger.ERROR, "Listing articles :" + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerCommunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }

        return result;
    }

    /**
     *
     * @return the article in remote service with id idArticle
     * @throws ServerCommunicationError
     */
    public static Article getArticle(int idArticle) throws ServerCommunicationError{

        Article result = null;
        try{
            String parameters =  "";
            String request = rc.serviceUrl + "article/" + idArticle;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned) TrustModifier.relaxHostChecking(connection);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty(CONTENTTYPE, URLENCODED);
            connection.setRequestProperty(AUTHORIZATION, getAuthTokenHeader());
            connection.setRequestProperty(CHARSET, UTF8);
            connection.setRequestProperty(CONTENTLENGTH, "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches (false);

            int httpResult =connection.getResponseCode();
            if(httpResult ==HttpURLConnection.HTTP_OK){
                String res = parseHttpStreamResult(connection);
                JSONObject object = ServiceCallUtils.readRestResultFromGetObject(res);
                result = new Article(object);
                Logger.log (Logger.INFO, " object (Article) retrieved");
            }else{
                throw new ServerCommunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log (Logger.ERROR, "Getting article :" + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerCommunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }

        return result;
    }

    public static int saveArticle(Article a) throws ServerCommunicationError{
        try{
            String request = rc.serviceUrl + "article";

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(CONTENTTYPE, "application/json");
            connection.setRequestProperty(AUTHORIZATION, getAuthTokenHeader());
            connection.setRequestProperty(CHARSET, UTF8);
            connection.setUseCaches (false);

            ServiceCallUtils.writeJSONParams(connection, a.toJSON());

            int httpResult =connection.getResponseCode();
            if(httpResult ==HttpURLConnection.HTTP_OK){
                String res = parseHttpStreamResult(connection);
                // get id from status ok when saved
                int id = ServiceCallUtils.readRestResultFromInsert(res);
                Logger.log(Logger.INFO, "Object inserted, returned id:" + id);
                return id;
            }else{
                throw new ServerCommunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR,"Inserting article ["+a+"] : " + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerCommunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }
    }

    public static void deleteArticle(int idArticle) throws ServerCommunicationError{
        try{
            String parameters =  "";
            String request = rc.serviceUrl + "article/" + idArticle;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if(rc.requireSelfSigned)
                TrustModifier.relaxHostChecking(connection);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty(CONTENTTYPE, "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty(AUTHORIZATION, getAuthTokenHeader());
            connection.setRequestProperty(CHARSET, UTF8);
            connection.setRequestProperty(CONTENTLENGTH, "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches (false);

            int httpResult =connection.getResponseCode();
            if(httpResult ==HttpURLConnection.HTTP_OK  || httpResult ==HttpURLConnection.HTTP_NO_CONTENT){
                Logger.log (Logger.INFO,"Article (id:"+idArticle+") deleted with status "+httpResult+":"+parseHttpStreamResult(connection));
            }else{
                throw new ServerCommunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Deleting article (id:"+idArticle+") : " + e.getClass() + " ( "+e.getMessage() + ")");
            throw new ServerCommunicationError(e.getClass() + " ( "+e.getMessage() + ")");
        }
    }

    //Function to check new articles on the server
    public static List<Article> getUpdates(String date) throws ServerCommunicationError {

        List<Article> result = new ArrayList<>();
        try {
            if(rc==null){
                Properties prop = new Properties();
                prop.setProperty("service_url","https://sanger.dia.fi.upm.es/pmd-task/");
                prop.setProperty("require_self_signed_cert","TRUE");
                ModelManager.configureConnection(prop);
            }
            String parameters = "";
            String request = rc.serviceUrl + "articlesFrom/" + date;

            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (rc.requireSelfSigned) TrustModifier.relaxHostChecking(connection);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty(CONTENTTYPE, URLENCODED);
            connection.setRequestProperty(AUTHORIZATION, getAuthTokenHeader());
            connection.setRequestProperty(CHARSET, UTF8);
            connection.setRequestProperty(CONTENTLENGTH, "" + Integer.toString(parameters.getBytes().length));
            connection.setUseCaches(false);

            int httpResult = connection.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK) {
                String res = parseHttpStreamResult(connection);
                List<JSONObject> objects = ServiceCallUtils.readRestResultFromList(res);
                for (JSONObject jsonObject : objects) {
                    result.add(new Article(jsonObject));
                }
                Logger.log(Logger.INFO, objects.size() + " new Articles retrieved");
            } else {
                throw new ServerCommunicationError(connection.getResponseMessage());
            }
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "Updating articles :" + e.getClass() + " ( " + e.getMessage() + ")");
            throw new ServerCommunicationError(e.getClass() + " ( " + e.getMessage() + ")");
        }

        return result;
    }

    public static void logout(Context context){
        rc.clear();
        SharedPreferences preferencia = context.getSharedPreferences("user_info",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("id_user", "");
        editor.putString("strApiKey", "");
        editor.putString("strIdAuthUser", "");
        editor.commit();
    }
}
