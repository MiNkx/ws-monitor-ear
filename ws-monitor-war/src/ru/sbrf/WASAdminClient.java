package ru.sbrf;

        import java.util.Properties;
        import java.util.ResourceBundle;

        import com.ibm.websphere.management.AdminClient;
        import com.ibm.websphere.management.AdminClientFactory;
        import com.ibm.websphere.management.exception.ConnectorException;


public class WASAdminClient {
    private String hostname = "localhost";    // default
    private String port = "8880";             // default

    private String username = "wasadmin";
    private String password = "wasadmin";
    private String connector_security_enabled = "true";
    private String connector_cache_disabled = "false";
    private String ssl_trustStore = "";
    private String ssl_keyStore = "";
    private String ssl_trustStorePassword = "";
    private String ssl_keyStorePassword = "";

    private String soap_client_properties;

    private AdminClient adminClient;

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public AdminClient create() throws ConnectorException {
        //TODO add input
        Properties props = new Properties();
        props.setProperty(AdminClient.CONNECTOR_TYPE, AdminClient.CONNECTOR_TYPE_SOAP);
        props.setProperty(AdminClient.CONNECTOR_HOST, hostname);
        props.setProperty(AdminClient.CONNECTOR_PORT, port);
        props.setProperty(AdminClient.CACHE_DISABLED, connector_cache_disabled);
        props.setProperty(AdminClient.CONNECTOR_SECURITY_ENABLED, connector_security_enabled);
/*        props.setProperty(AdminClient.CONNECTOR_AUTO_ACCEPT_SIGNER, "true");
        props.setProperty("javax.net.ssl.trustStore", ssl_trustStore);
        props.setProperty("javax.net.ssl.keyStore", ssl_keyStore);
        props.setProperty("javax.net.ssl.trustStorePassword", ssl_trustStorePassword);
        props.setProperty("javax.net.ssl.keyStorePassword", ssl_keyStorePassword);*/
        props.setProperty(AdminClient.USERNAME, username);
        props.setProperty(AdminClient.PASSWORD, password);
        try {
            adminClient = AdminClientFactory.createAdminClient(props);
        }catch (ConnectorException e){
            e.printStackTrace();
        }

        return adminClient;
    }

}