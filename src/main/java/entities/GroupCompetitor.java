/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "group_competitor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupCompetitor.findAll", query = "SELECT g FROM GroupCompetitor g"),
    @NamedQuery(name = "GroupCompetitor.findByIdGroupCompetitor", query = "SELECT g FROM GroupCompetitor g WHERE g.idGroupCompetitor = :idGroupCompetitor")})
public class GroupCompetitor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_group_competitor")
    private Integer idGroupCompetitor;
    @JoinColumn(name = "id_competitor", referencedColumnName = "id_competitor")
    @ManyToOne(optional = false)
    private Competitor idCompetitor;
    @JoinColumn(name = "id_group", referencedColumnName = "id_group")
    @ManyToOne(optional = false)
    private Groupp idGroup;

    public GroupCompetitor() {
    }

    public GroupCompetitor(Integer idGroupCompetitor) {
        this.idGroupCompetitor = idGroupCompetitor;
    }

    public Integer getIdGroupCompetitor() {
        return idGroupCompetitor;
    }

    public void setIdGroupCompetitor(Integer idGroupCompetitor) {
        this.idGroupCompetitor = idGroupCompetitor;
    }

    public Competitor getIdCompetitor() {
        return idCompetitor;
    }

    public void setIdCompetitor(Competitor idCompetitor) {
        this.idCompetitor = idCompetitor;
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
        hash += (idGroupCompetitor != null ? idGroupCompetitor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupCompetitor)) {
            return false;
        }
        GroupCompetitor other = (GroupCompetitor) object;
        if ((this.idGroupCompetitor == null && other.idGroupCompetitor != null) || (this.idGroupCompetitor != null && !this.idGroupCompetitor.equals(other.idGroupCompetitor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.GroupCompetitor[ idGroupCompetitor=" + idGroupCompetitor + " ]";
    }
    
}
