package ru.sbrf.controller;

import javax.ejb.Remote;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import com.ibm.websphere.management.AdminClient;
/**
 * Created by kozmi on 2016-01-07.
 */
@Remote
public interface DatabaseService {
    UUID addServer(String user, String host, Integer port) throws Exception;
    void removeServer(String id) throws Exception;
    String getServers() throws Exception;
    String getServerByUser(String user) throws Exception;
    String test() throws Exception;
}
