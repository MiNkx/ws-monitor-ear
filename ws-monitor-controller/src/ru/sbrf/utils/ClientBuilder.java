package ru.sbrf.utils;

import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.management.exception.ConnectorException;
import java.util.Properties;

/**
 * Created by kozmi on 2016-01-09.
 */
public class ClientBuilder {

    private final static String connector_security_enabled = "true";

    private final static String connector_cache_disabled = "false";

    public static AdminClient create(String username, String password, String host, Integer port) throws ConnectorException {
        AdminClient adminClient = null;
        Properties props = new Properties();
        props.setProperty(AdminClient.CONNECTOR_TYPE, AdminClient.CONNECTOR_TYPE_SOAP);
        props.setProperty(AdminClient.CONNECTOR_HOST, host);
        props.setProperty(AdminClient.CONNECTOR_PORT, port.toString());
        props.setProperty(AdminClient.CACHE_DISABLED, connector_cache_disabled);
        props.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, connector_security_enabled);
        props.setProperty(AdminClient.USERNAME, username);
        props.setProperty(AdminClient.PASSWORD, password);
        try {
            adminClient = AdminClientFactory.createAdminClient(props);
        }catch (ConnectorException e){
            System.out.println("ERROR creating adminClient: "+e.getMessage());
            e.printStackTrace();
        }
        System.out.println(adminClient.toString());
        return adminClient;
    }

/*    public static PasswordCredential getCredential() throws Exception{

        System.out.println("getCredential invoked");
        Map map = new HashMap();
        map.put(Constants.MAPPING_ALIAS, "master");
        CallbackHandler callbackHandler = WSMappingCallbackHandlerFactory.getInstance().getCallbackHandler(map, null);
        LoginContext loginContext = new LoginContext("DefaultPrincipalMapping", callbackHandler);
        loginContext.login();
        Subject subject = loginContext.getSubject();
        Set credentials = subject.getPrivateCredentials();
        PasswordCredential passwordCredential = (PasswordCredential) credentials.iterator().next();

        String user = passwordCredential.getUserName();
        String password = new String(passwordCredential.getPassword());
        System.out.println("Credentials: "+user+" "+password);
        return passwordCredential;
    }*/
}
