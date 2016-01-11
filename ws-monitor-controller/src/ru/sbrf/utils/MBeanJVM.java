package ru.sbrf.utils;

import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.exception.ConnectorException;
import org.json.JSONObject;

import javax.management.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kozmi on 2016-01-09.
 */
public class MBeanJVM {

    public static Set queryMBeans(AdminClient ac) throws MalformedObjectNameException,
            NullPointerException, ConnectorException {
        String query = "WebSphere:type=JVM,*";
        ObjectName queryName = new ObjectName(query);
        return ac.queryNames(queryName, null);
    }

    public static HashMap<String, JSONObject> getJVMinfo(Set mBeanSet, AdminClient ac) throws
            AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException,
            MalformedObjectNameException,
            ConnectorException {
        HashMap<String, JSONObject> output = new HashMap<String, JSONObject>();
        HashMap<String, JSONObject> outputHolder = new HashMap<String, JSONObject>();

        if(mBeanSet != null) {
            Iterator i = mBeanSet.iterator();
            while (i.hasNext()) {
                HashMap<String, String> params = new HashMap<String, String>();
                ObjectName on = (ObjectName)i.next();

                //Getting web container and thread pools results for current server;
                Set tpMbeans = MBeanThread.queryMBeans(ac, on.getKeyProperty("node"), on.getKeyProperty("process"));
                HashMap<String, JSONObject> tpResult = MBeanThread.getTheadPoolConfig(tpMbeans, ac);

                Set wcPmiMbeans = PMIWebContainer.queryMBeans(ac, on.getKeyProperty("node"), on.getKeyProperty("process"));
                HashMap<String, String> wcPmiResult= PMIWebContainer.getWebContainerInfo(wcPmiMbeans, ac);

                params.put("freeMemory", ac.getAttribute(on, "freeMemory").toString());
                params.put("maxHeapDumpsOnDisk", ac.getAttribute(on, "maxHeapDumpsOnDisk").toString());
                params.put("maxMemory", ac.getAttribute(on, "maxMemory").toString());
                params.put("heapSize", ac.getAttribute(on, "heapSize").toString());

                outputHolder.put("jvm", new JSONObject(params));
                outputHolder.put("info", new JSONObject(on.getKeyPropertyList()));
                outputHolder.put("threadPoolConfig", new JSONObject(tpResult));
                outputHolder.put("webContainerPerf", new JSONObject(wcPmiResult));

                output.put(on.getKeyProperty("node")+"_"+on.getKeyProperty("process"), new JSONObject(outputHolder));
            }
        }
        return output;
    }

}
