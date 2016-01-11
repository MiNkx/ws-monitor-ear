package ru.sbrf.utils;

import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.exception.ConnectorException;
import com.ibm.websphere.pmi.stat.*;
import org.json.JSONObject;

import javax.management.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kozmi on 2016-01-10.
 */
public class PMIWebContainer {

    public static Set queryMBeans(AdminClient ac, String node, String process) throws MalformedObjectNameException,
            NullPointerException, ConnectorException {
        String query = "WebSphere:node=" + node + ",process=" + process + ",type=Perf,*";
        ObjectName queryName = new ObjectName(query);
        return ac.queryNames(queryName, null);
    }

    public static HashMap<String, String> getWebContainerInfo(Set mBeanSet, AdminClient ac) throws
            AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException,
            ConnectorException {
        HashMap<String, String> res = new HashMap<String, String>();
        if (mBeanSet != null) {
            Iterator i = mBeanSet.iterator();
            while (i.hasNext()) {

                ObjectName on = (ObjectName) i.next();
                String[] signature = new String[]{"[Lcom.ibm.websphere.pmi.stat.StatDescriptor;", "java.lang.Boolean"};
                StatDescriptor webContainerPoolSD = new StatDescriptor(new String[]{WSThreadPoolStats.NAME, "WebContainer"});
                Object[] params = new Object[]{new StatDescriptor[]{webContainerPoolSD}, new Boolean(true)};
                WSStats[] wsStats = (WSStats[]) ac.invoke(on, "getStatsArray", params, signature);
                // Only 1 stats package should be returned, since we were specific about what to get
                // We know that the Stats should be from BoundedRangeStatistic
                // get the Active Count
                WSStatistic[] dataMembers = wsStats[0].getStatistics();
                for (int j = 0; j < dataMembers.length; j++) {
                    try {
                        WSRangeStatistic wsrStats = (WSRangeStatistic) dataMembers[j];
                        res.put(wsrStats.getName(), Long.toString(wsrStats.getCurrent()));
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }
        return res;
    }

}
