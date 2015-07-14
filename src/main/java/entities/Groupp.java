/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author java
 */
@Entity
@Table(name = "groupp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groupp.findAll", query = "SELECT g FROM Groupp g"),
    @NamedQuery(name = "Groupp.findByIdGroup", query = "SELECT g FROM Groupp g WHERE g.idGroup = :idGroup"),
    @NamedQuery(name = "Groupp.findByGroupName", query = "SELECT g FROM Groupp g WHERE g.groupName = :groupName"),
    @NamedQuery(name = "Groupp.findByStartDate", query = "SELECT g FROM Groupp g WHERE g.startDate = :startDate"),
    @NamedQuery(name = "Groupp.findByEndDate", query = "SELECT g FROM Groupp g WHERE g.endDate = :endDate"),
    @NamedQuery(name = "Groupp.findByVersion", query = "SELECT g FROM Groupp g WHERE g.version = :version")})
public class Groupp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_group")
    private Long idGroup;
    @Basic(optional = false)
    @NotNull
    @Column(name = "group_name")
    private Character groupName;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGroup")
    private List<CompetitorMatchGroup> competitorMatchGroupList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGroup")
    private List<GroupCompetition> groupCompetitionList;

    public Groupp() {
    }

    public Groupp(Long idGroup) {
        this.idGroup = idGroup;
    }

    public Groupp(Long idGroup, Character groupName, long version) {
        this.idGroup = idGroup;
        this.groupName = groupName;
        this.version = version;
    }

    public Long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Long idGroup) {
        this.idGroup = idGroup;
    }

    public Character getGroupName() {
        return groupName;
    }

    public void setGroupName(Character groupName) {
        this.groupName = groupName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @XmlTransient
    public List<CompetitorMatchGroup> getCompetitorMatchGroupList() {
        return competitorMatchGroupList;
    }

    public void setCompetitorMatchGroupList(List<CompetitorMatchGroup> competitorMatchGroupList) {
        this.competitorMatchGroupList = competitorMatchGroupList;
    }

    @XmlTransient
    public List<GroupCompetition> getGroupCompetitionList() {
        return groupCompetitionList;
    }

    public void setGroupCompetitionList(List<GroupCompetition> groupCompetitionList) {
        this.groupCompetitionList = groupCompetitionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGroup != null ? idGroup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groupp)) {
            return false;
        }
        Groupp other = (Groupp) object;
        if ((this.idGroup == null && other.idGroup != null) || (this.idGroup != null && !this.idGroup.equals(other.idGroup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Groupp[ idGroup=" + idGroup + " ]";
    }
    
}
