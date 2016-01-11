package ru.sbrf.controller;

import com.ibm.websphere.management.AdminClient;
import org.json.JSONArray;
import ru.sbrf.utils.Converter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kozmi on 2016-01-07.
 */
@Singleton
public class DatabaseServiceBean implements DatabaseService, DatabaseServiceLocal {

    public DatabaseServiceBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("===== Post Init START =====");
        try {
            setDs();
            setTableName("MONITOR.USERSERVERS");
            System.out.println("DataSource obtained!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("===== Post Init DONE =====");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("===== Pre Destroy =====");
        /*try {
            dsConnection.close();
            System.out.println("Connection closed!");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*System.out.println("===== Pre Destroy DONE =====");*/
    }

    private DataSource ds;


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private String tableName;

    public void setDs() throws Exception {
        InitialContext ctx = new InitialContext();
        this.ds = (DataSource) ctx.lookup("jdbc/monitor");

    }

    @Override
    public UUID addServer(String user, String host, Integer port) throws Exception{
        Connection conn = null;
        PreparedStatement prp = null;
        UUID id = UUID.randomUUID();
        try {
            conn = ds.getConnection();
            prp = conn.prepareStatement("INSERT INTO "+tableName+" (id, name, host, port) VALUES (?,?,?,?)");
            prp.setString(1, id.toString());
            prp.setString(2, user);
            prp.setString(3, host);
            prp.setInt(4, port);
            Integer rowsUpdated = prp.executeUpdate();
            System.out.println("ROWS UPDATED: "+rowsUpdated);
        } catch (SQLException e) {
            System.out.println("SQL Exception in addServer method: "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
            if (prp != null) prp.close();
        }
        return id;
    }

    @Override
    public void removeServer(String id) throws Exception{
        Connection conn = null;
        PreparedStatement prp = null;
        try {
            conn = ds.getConnection();
            prp = conn.prepareStatement("DELETE FROM "+tableName+" WHERE id=?");
            prp.setString(1, id);
            prp.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL Exception in removeServer method: "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
            if (prp != null) prp.close();
        }
    }

    @Override
    public String getServers() throws Exception{
        ResultSet res;
        JSONArray jsonRes;
        String jsonString = null;
        Connection conn = null;
        Statement stmt = null;
        String query = "SELECT * FROM "+tableName;

        try {
            conn = ds.getConnection();
            stmt = conn.createStatement();
            res = stmt.executeQuery(query);
            jsonRes = Converter.convertResultSetIntoJSON(res);
            jsonString = jsonRes.toString();
        } catch (SQLException e) {
            System.out.println("SQL Exception in getServers method: "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
            if (stmt != null) stmt.close();
        }
        return jsonString;
    }

    @Override
    public String getServerByUser(String user) throws Exception{
        ResultSet res;
        JSONArray jsonRes;
        String jsonString = null;
        Connection conn = null;
        PreparedStatement prp = null;

        try {
            conn = ds.getConnection();
            prp = conn.prepareStatement("SELECT * FROM "+tableName+" WHERE NAME=?");
            prp.setString(1, user);
            res = prp.executeQuery();
            jsonRes = Converter.convertResultSetIntoJSON(res);
            jsonString = jsonRes.toString();
        } catch (SQLException e) {
            System.out.println("SQL Exception in removeServer method: "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
            if (prp != null) prp.close();
        }
        return jsonString;
    }

    @Override
    public String test() throws Exception {
        return "Database service works!";
    }
}

