package ru.sbrf;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.Set;

        import javax.management.AttributeNotFoundException;
        import javax.management.InstanceNotFoundException;
        import javax.management.MBeanException;
        import javax.management.MalformedObjectNameException;
        import javax.management.ObjectName;
        import javax.management.ReflectionException;

        import com.ibm.websphere.management.AdminClient;
        import com.ibm.websphere.management.exception.ConnectorException;
        import com.ibm.websphere.pmi.stat.*;
        import org.json.JSONObject;

public class JavaVirtualMachine {
    private String heapSize;
    private String maxMemory;
    private Integer maxHeapDumpsOnDisk;
    private String freeMemory;

    private ObjectName mBean;
    private Set mBeans;
    private AdminClient adminClient;

    public JavaVirtualMachine() {
    }

    public JavaVirtualMachine(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public void setAdminClient(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public Set queryMBeans() throws MalformedObjectNameException,
            NullPointerException, ConnectorException {
        String query = "WebSphere:*,type=Perf";
        ObjectName queryName = new ObjectName(query);
        mBeans = adminClient.queryNames(queryName, null);

        return mBeans;
    }

    public Set getMBeans() {
        return mBeans;
    }

    public void setMBeans(Set mBeans) {
        this.mBeans = mBeans;
    }

    public ObjectName getMBean() {
        return mBean;
    }

    public void setMBean(ObjectName mBean) {
        this.mBean = mBean;
    }

    public String getHeapSize(ObjectName mBean) throws AttributeNotFoundException,
            InstanceNotFoundException,
            MBeanException, ReflectionException,
            ConnectorException {
        heapSize = (String) adminClient.getAttribute(mBean, "heapSize");
        return heapSize;
    }

    public String getMaxMemory(ObjectName mBean) throws AttributeNotFoundException,
            InstanceNotFoundException, MBeanException,
            ReflectionException, ConnectorException {
        maxMemory = (String) adminClient.getAttribute(mBean, "maxMemory");
        return maxMemory;
    }

    public Integer getMaxHeapDumpsOnDisk(ObjectName mBean) throws AttributeNotFoundException,
            InstanceNotFoundException, MBeanException,
            ReflectionException, ConnectorException {
        maxHeapDumpsOnDisk = (Integer) adminClient.getAttribute(mBean, "maxHeapDumpsOnDisk");
        return maxHeapDumpsOnDisk;
    }

    public ArrayList<String> getFreeMemory(Set mBeanSet) throws AttributeNotFoundException,
            InstanceNotFoundException, MBeanException,
            ReflectionException, ConnectorException {
        ArrayList<String> output = new ArrayList<String>();
        HashMap<String, String> res = new HashMap<String, String>();
        ObjectName on;
        if (mBeanSet != null) {
            Iterator i = mBeanSet.iterator();
            while (i.hasNext()) {
                on = (ObjectName) i.next();
                String[] signature = new String[]{"[Lcom.ibm.websphere.pmi.stat.StatDescriptor;", "java.lang.Boolean"};
                StatDescriptor webContainerPoolSD = new StatDescriptor(new String[]{WSThreadPoolStats.NAME, "WebContainer"});
                Object[] params = new Object[]{new StatDescriptor[]{webContainerPoolSD}, new Boolean(true)};
                WSStats[] wsStats = (WSStats[]) adminClient.invoke(on, "getStatsArray", params, signature);
                // Only 1 stats package should be returned, since we were specific about what to get
                // We know that the Stats should be from BoundedRangeStatistic
                // get the Active Count
                WSStatistic[] dataMembers = wsStats[0].getStatistics();
                for (int j = 0; j < dataMembers.length; j++) {
                    WSRangeStatistic wsrStats = (WSRangeStatistic) dataMembers[j];
                    System.out.println(wsrStats.getName() + " : " + wsrStats.getCurrent());
                    res.put(wsrStats.getName(), Long.toString(wsrStats.getCurrent()));
                }
                JSONObject x = new JSONObject(res);
                output.add(x.toString());

            }
        }
        return output;
    }
}
