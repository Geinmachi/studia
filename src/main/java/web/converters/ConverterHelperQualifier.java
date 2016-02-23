/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.converters;

import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author java
 */
public class ConverterHelperQualifier extends AnnotationLiteral<ConverterHelper> implements ConverterHelper {

    private final String viewId;

    public ConverterHelperQualifier(String viewId) {
        this.viewId = viewId;
    }
    
    @Override
    public String viewId() {
        return this.viewId;
    }
    
}
