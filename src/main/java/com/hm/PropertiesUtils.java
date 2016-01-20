package com.hm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ant_shake_tree on 15/10/22.
 */
public class PropertiesUtils {
    private static final    Properties properties;
    static {
        InputStream inputStream= com.hm.RedissionHelpler.class.getResourceAsStream("/cache.properties");
         properties= new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getProperty(String key){
        return properties.getProperty(key);
    }
    public static int getIntProperty(String key){
        return Integer.parseInt(properties.getProperty(key));
    }

}
