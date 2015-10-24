/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import web.utils.JsfUtils;

/**
 *
 * @author java
 */
public class ResourceBundleUtil {
    
    public static String getResourceBundleBusinessProperty(String propertyName) {
        ResourceBundle rb = ResourceBundle.getBundle("properties.business.business", Locale.ROOT);
        
        return rb.getString(propertyName);
    }
    
    public static String getResourceBundleProperty(String propertyName) {
        ResourceBundle rb = ResourceBundle.getBundle("properties.language.locale", JsfUtils.getLocale());
        
        return rb.getString(propertyName);
    }
}
