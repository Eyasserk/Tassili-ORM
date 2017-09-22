/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.boot;

import com.upspain.tassili.context.ContextConfiguration;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author ykantour
 */
public class TassiliBoot implements ServletContextListener{
    private static final Logger LOGGER = Logger.getLogger(TassiliBoot.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.upspain.tassili.context.ContextConfiguration");
        } catch (ClassNotFoundException ex) {
            LOGGER.severe("Tassili Boot Class not found");
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ContextConfiguration.shutDown();
    }
    
}
