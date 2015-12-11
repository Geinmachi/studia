/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.validators;

import org.primefaces.model.DualListModel;

/**
 *
 * @author java
 */
public interface DuplicatedCompetitors extends CompetitorsDualListSetter {
    
    void setDuplicatedCompetitorFlag(boolean duplicatedCompetitorFlag);
}
