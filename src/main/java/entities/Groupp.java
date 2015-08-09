/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
public class Groupp implements Serializable, Comparable<Groupp> {
    private static final long serialVersionUID = 1L;
    
    @Transient
    private UUID uuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_group")
    private Integer idGroup;
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
    @OneToMany(mappedBy = "idGroup")
    private List<CompetitorMatchGroup> competitorMatchGroupList = new ArrayList<>();
    @OneToMany(mappedBy = "idGroup")
    private List<GroupCompetitor> groupCompetitornList = new ArrayList<>();

    public Groupp() {
    }

    public Groupp(UUID uuid) {
        this.uuid = uuid;
    }
    public Groupp(Integer idGroup) {
        this.idGroup = idGroup;
    }

    public Groupp(Integer idGroup, Character groupName, long version) {
        this.idGroup = idGroup;
        this.groupName = groupName;
        this.version = version;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
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
    public List<GroupCompetitor> getGroupCompetitornList() {
        return groupCompetitornList;
    }

    public void setGroupCompetitornList(List<GroupCompetitor> groupCompetitornList) {
        this.groupCompetitornList = groupCompetitornList;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.uuid);
        hash = 11 * hash + Objects.hashCode(this.idGroup);
        hash = 11 * hash + Objects.hashCode(this.groupName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Groupp other = (Groupp) obj;
        
        if (this.uuid != null && other.uuid != null) {
            if (Objects.equals(this.uuid, other.uuid)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (!Objects.equals(this.idGroup, other.idGroup)) {
            return false;
        }
        if (!Objects.equals(this.groupName, other.groupName)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "entities.Groupp[ idGroup=" + idGroup + " ]";
    }

    @Override
    public int compareTo(Groupp o) {
        return this.groupName.compareTo(o.getGroupName());
    }
    
}
