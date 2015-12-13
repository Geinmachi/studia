/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.Organizer;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface OrganizerFacadeLocal {

    void create(Organizer organizer);

    void edit(Organizer organizer) throws ApplicationException;

    void remove(Organizer organizer);

    Organizer find(Object id);

    List<Organizer> findAll();

    List<Organizer> findRange(int[] range);

    int count();
    
}
