package ru.sbrf;

import ru.sbrf.controller.*;

import javax.naming.InitialContext;

/**
 * Created by kozmi on 2016-01-07.
 */
public class Client {

    public void testEjb() throws Exception{
        InitialContext ctx = new InitialContext();
        System.out.println(" InitialContext ctx.toString() "+ ctx.toString());
        System.out.println(" InitialContext ctx.getNameInNamespace() "+ ctx.getNameInNamespace());
        DatabaseService cs = (DatabaseService) ctx.lookup("ru.sbrf.controller.DatabaseService");
        String wohoo = cs.test();
        System.out.println(wohoo);
    }
}
