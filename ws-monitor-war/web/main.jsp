<%--
  Created by IntelliJ IDEA.
  User: kozmi
  Date: 2016-01-03
  Time: 15:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="ru.sbrf.WASAdminClient" %>
<%@ page import="ru.sbrf.JavaVirtualMachine" %>
<%@ page import="com.ibm.websphere.management.AdminClient" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ru.sbrf.Client" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WAS monitor application v0.1b</title>
    <%--DEBUG--%>
    <%
        WASAdminClient wasac = new WASAdminClient();
        AdminClient ac = wasac.create();
        JavaVirtualMachine jvm = new JavaVirtualMachine(ac);
        Set mBeanSet = jvm.queryMBeans();
        ArrayList<String> wohoo = jvm.getFreeMemory(mBeanSet);

        Client cl = new Client();
        cl.testEjb();
    %>

</head>
<body>
<script>
    window.currentMonitorUser = "<%out.print(request.getRemoteUser());%>"
</script>
<%= wohoo %>
<% out.print(request.getRemoteUser()); %>
</body>
</html>
