package es.upm.etsiinf.upmnews.utils.network;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class RESTConnection {
    private Properties ini  = null;
    String idUser = null;
    String authType;
    String apikey;

    void clear(){
        idUser = null;
        authType = null;
        apikey = null;
    }

    boolean isAdministrator = false;

    String serviceUrl ;

    boolean requireSelfSigned = false;

    public static final String ATTR_LOGIN_USER = "username";
    public static final String ATTR_LOGIN_PASS = "password";
    private static final String ATTR_SERVICE_URL = "service_url";
    private static final String ATTR_REQUIRE_SELF_CERT = "require_self_signed_cert";
    private static final String ATTR_PROXY_HOST = "";
    private static final String ATTR_PROXY_PORT = "";
    private static final String ATTR_PROXY_USER = "";
    private static final String ATTR_PROXY_PASS = "";
    public static final String ATTR_APACHE_AUTH_USER = "";
    public static final String ATTR_APACHE_AUTH_PASS = "";

    RESTConnection(Properties ini){
        this.ini = ini;
        if (!ini.containsKey(ATTR_SERVICE_URL)){
            throw new IllegalArgumentException("Required attribute '"+ ATTR_SERVICE_URL+"' not found!");
        }

        // disable auth from self signed certificates
        requireSelfSigned =  (ini.containsKey(ATTR_REQUIRE_SELF_CERT) && ((String)ini.get(ATTR_REQUIRE_SELF_CERT)).equalsIgnoreCase("TRUE"));

        // add proxy http/https to the system
        if (ini.contains(ATTR_PROXY_HOST) && ini.contains(ATTR_PROXY_PORT)){
            String proxyHost = (String)ini.get(ATTR_PROXY_HOST);
            String proxyPort = (String)ini.get(ATTR_PROXY_PORT);

            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort);
        }

        if (ini.contains(ATTR_PROXY_USER) && ini.contains(ATTR_PROXY_PASS))	{
            final String proxyUser = (String)ini.get(ATTR_PROXY_USER);
            final String proxyPassword = (String)ini.get(ATTR_PROXY_PASS);

            System.setProperty("http.proxyUser", proxyUser);
            System.setProperty("http.proxyPassword", proxyPassword);
            System.setProperty("https.proxyUser", proxyUser);
            System.setProperty("https.proxyPassword", proxyPassword);

            Authenticator.setDefault(
                    new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                        }
                    }
            );
        }

        serviceUrl = ini.getProperty(ATTR_SERVICE_URL);
    }
}
