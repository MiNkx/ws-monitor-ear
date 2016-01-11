package ru.sbrf.rest;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kozmi on 2016-01-08.
 */
public class RestFulServiceConfig extends Application{
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(RestFulService.class);
        return classes;
    }
}
