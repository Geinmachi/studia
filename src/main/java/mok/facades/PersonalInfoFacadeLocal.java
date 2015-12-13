/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mok.facades;

import entities.PersonalInfo;
import exceptions.ApplicationException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author java
 */
@Remote
public interface PersonalInfoFacadeLocal {

    void create(PersonalInfo personalInfo);

    void edit(PersonalInfo personalInfo) throws ApplicationException;

    void remove(PersonalInfo personalInfo);

    PersonalInfo find(Object id);

    List<PersonalInfo> findAll();

    List<PersonalInfo> findRange(int[] range);

    int count();
    
}
