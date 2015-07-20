/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author java
 */
@Named(value = "competitionController")
@SessionScoped
public class CompetitionController implements Serializable {

    /**
     * Creates a new instance of CompetitionController
     */
    public CompetitionController() {
    }
    
}
