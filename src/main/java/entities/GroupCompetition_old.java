/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "group_competition")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupCompetition.findAll", query = "SELECT g FROM GroupCompetition g"),
    @NamedQuery(name = "GroupCompetition.findByIdGroupCompetition", query = "SELECT g FROM GroupCompetition g WHERE g.idGroupCompetition = :idGroupCompetition")})
public class GroupCompetition_old implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_group_competition")
    private Integer idGroupCompetition;
    @JoinColumn(name = "id_competition", referencedColumnName = "id_competition")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Competition idCompetition;
    @JoinColumn(name = "id_group", referencedColumnName = "id_group")
    @ManyToOne(optional = false)
    private Groupp idGroup;

    public GroupCompetition_old() {
    }

    public GroupCompetition_old(Integer idGroupCompetition) {
        this.idGroupCompetition = idGroupCompetition;
    }

    public Integer getIdGroupCompetition() {
        return idGroupCompetition;
    }

    public void setIdGroupCompetition(Integer idGroupCompetition) {
        this.idGroupCompetition = idGroupCompetition;
    }

    public Competition getIdCompetition() {
        return idCompetition;
    }

    public void setIdCompetition(Competition idCompetition) {
        this.idCompetition = idCompetition;
    }

    public Groupp getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Groupp idGroup) {
        this.idGroup = idGroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGroupCompetition != null ? idGroupCompetition.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupCompetition_old)) {
            return false;
        }
        GroupCompetition_old other = (GroupCompetition_old) object;
        if ((this.idGroupCompetition == null && other.idGroupCompetition != null) || (this.idGroupCompetition != null && !this.idGroupCompetition.equals(other.idGroupCompetition))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.GroupCompetition[ idGroupCompetition=" + idGroupCompetition + " ]";
    }
    
}
