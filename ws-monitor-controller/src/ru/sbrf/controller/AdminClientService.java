package ru.sbrf.controller;

import javax.ejb.Remote;

/**
 * Created by kozmi on 2016-01-09.
 */
@Remote
public interface AdminClientService {
    String getServerInfo(String servers) throws Exception;
    String test();
}
