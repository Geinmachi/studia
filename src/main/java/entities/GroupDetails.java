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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "group_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupDetails.findAll", query = "SELECT g FROM GroupDetails g"),
    @NamedQuery(name = "GroupDetails.findByIdGroupDetails", query = "SELECT g FROM GroupDetails g WHERE g.idGroupDetails = :idGroupDetails"),
    @NamedQuery(name = "GroupDetails.findByStartDate", query = "SELECT g FROM GroupDetails g WHERE g.startDate = :startDate"),
    @NamedQuery(name = "GroupDetails.findByEndDate", query = "SELECT g FROM GroupDetails g WHERE g.endDate = :endDate"),
    @NamedQuery(name = "GroupDetails.findByVersion", query = "SELECT g FROM GroupDetails g WHERE g.version = :version")})
public class GroupDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_group_details")
    private Integer idGroupDetails;
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
    @JoinColumn(name = "id_group_name", referencedColumnName = "id_group_name")
    @ManyToOne(optional = false)
    private GroupName idGroupName;
    @JoinColumn(name = "id_competition", referencedColumnName = "id_competition")
    @ManyToOne(optional = false)
    private Competition competition;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGroupDetails")
//    private List<GroupCompetitor> groupCompetitorList;

    public GroupDetails() {
    }

    public GroupDetails(Integer idGroupDetails) {
        this.idGroupDetails = idGroupDetails;
    }

    public GroupDetails(Integer idGroupDetails, long version) {
        this.idGroupDetails = idGroupDetails;
        this.version = version;
    }

    public Integer getIdGroupDetails() {
        return idGroupDetails;
    }

    public void setIdGroupDetails(Integer idGroupDetails) {
        this.idGroupDetails = idGroupDetails;
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

    public GroupName getIdGroupName() {
        return idGroupName;
    }

    public void setIdGroupName(GroupName idGroupName) {
        this.idGroupName = idGroupName;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

//    @XmlTransient
//    public List<GroupCompetitor> getGroupCompetitorList() {
//        return groupCompetitorList;
//    }
//
//    public void setGroupCompetitorList(List<GroupCompetitor> groupCompetitorList) {
//        this.groupCompetitorList = groupCompetitorList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGroupDetails != null ? idGroupDetails.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupDetails)) {
            return false;
        }
        GroupDetails other = (GroupDetails) object;
        if ((this.idGroupDetails == null && other.idGroupDetails != null) || (this.idGroupDetails != null && !this.idGroupDetails.equals(other.idGroupDetails))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.GroupDetails[ idGroupDetails=" + idGroupDetails + " ]";
    }
    
}
