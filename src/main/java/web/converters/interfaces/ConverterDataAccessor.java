/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters.interfaces;

import java.util.List;
import web.qualifiers.BackingBean;

/**
 *
 * @author java
 */
public interface ConverterDataAccessor<T> {
    
    List<T> getFetchedData();
}
