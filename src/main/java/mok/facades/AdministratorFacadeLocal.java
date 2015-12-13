/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.AccessLevel;
import entities.Administrator;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface AdministratorFacadeLocal {

    void create(Administrator administrator);

    void edit(Administrator administrator) throws ApplicationException;

    void remove(Administrator administrator);

    Administrator find(Object id);

    List<Administrator> findAll();

    List<Administrator> findRange(int[] range);

    int count();

    public AccessLevel createWithReturn(Administrator administrator);
    
}
