package ru.sbrf.utils;

import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.exception.ConnectorException;
import org.json.JSONObject;

import javax.management.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kozmi on 2016-01-10.
 */
public class MBeanThread {

    public static Set queryMBeans(AdminClient ac, String node, String process) throws MalformedObjectNameException,
            NullPointerException, ConnectorException {
        String query = "WebSphere:node=" + node + ",process=" + process + ",type=ThreadPool,*";
        ObjectName queryName = new ObjectName(query);
        return ac.queryNames(queryName, null);
    }

    public static HashMap<String, JSONObject> getTheadPoolConfig(Set mBeanSet, AdminClient ac) throws
            AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException,
            ConnectorException {
        HashMap<String, JSONObject> output = new HashMap<String, JSONObject>();

        if(mBeanSet != null) {
            Iterator i = mBeanSet.iterator();
            while (i.hasNext()) {
                HashMap<String, String> params = new HashMap<String, String>();
                ObjectName on = (ObjectName)i.next();
                params.put("maximumSize", ac.getAttribute(on, "maximumSize").toString());
                params.put("minimumSize", ac.getAttribute(on, "minimumSize").toString());
                output.put(ac.getAttribute(on, "name").toString(), new JSONObject(params));
              }
        }
        return output;
    }
}
