package ru.sbrf.controller;

import com.ibm.websphere.management.AdminClient;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.sbrf.utils.ClientBuilder;
import ru.sbrf.utils.MBeanJVM;
import ru.sbrf.utils.MBeanThread;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.naming.InitialContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kozmi on 2016-01-09.
 */
@Stateful
public class AdminClientServiceBean implements AdminClientService {

    public AdminClientServiceBean() {

    }

    private String currentServers = null;
    private HashMap<String, AdminClient> adminClients;
    private String user = null;
    private String password = null;

    public void refreshAdminClients() {
        this.adminClients = new HashMap<String, AdminClient>();
    }

    @PostConstruct
    public void init() {
        System.out.println("Admin Client Service POST CONSTRUCT");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Admin Client Service PRE DESTROY");
    }

    @Override
    public String getServerInfo(String servers) throws Exception {
        if (user == null || password == null) {
            InitialContext ctx = new InitialContext();
            user = (String) ctx.lookup("jndi/masteruser");
            password = (String) ctx.lookup("jndi/masterpass");
        }
        if (servers != currentServers) {
            currentServers = servers;
            refreshAdminClients();
            JSONArray jsonArr = new JSONArray(servers);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject item = jsonArr.getJSONObject(i);
                String host = item.getString("host");
                Integer port = item.getInt("port");
                AdminClient client = ClientBuilder.create(user, password, host, port);

                System.out.println(client.toString());

                adminClients.put(item.getString("id"), client);

            }
        }

        HashMap<String, JSONObject> result = new HashMap<String, JSONObject>();
        HashMap<String, JSONObject> resHolder;

        for (Map.Entry<String, AdminClient> entry : adminClients.entrySet()) {
            String id = entry.getKey();
            AdminClient ac = entry.getValue();
            Set JVMmBeans = MBeanJVM.queryMBeans(ac);
            resHolder = MBeanJVM.getJVMinfo(JVMmBeans, ac);
            result.put(id, new JSONObject(resHolder));
        }
        JSONObject jsonResult = new JSONObject(result);
        return jsonResult.toString();
    }

    @Override
    public String test() {
        return "adminClient up and running!";
    }
}
