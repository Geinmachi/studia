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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "group_name")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupName.findAll", query = "SELECT g FROM GroupName g"),
    @NamedQuery(name = "GroupName.findByIdGroupName", query = "SELECT g FROM GroupName g WHERE g.idGroupName = :idGroupName"),
    @NamedQuery(name = "GroupName.findByGroupName", query = "SELECT g FROM GroupName g WHERE g.groupName = :groupName")})
public class GroupName implements Serializable, Comparable<GroupName> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_group_name")
    private Integer idGroupName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "group_name")
    private Character groupName;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idGroupName")
//    private List<GroupDetails> groupDetailsList;

    public GroupName() {
    }

    public GroupName(Integer idGroupName) {
        this.idGroupName = idGroupName;
    }

    public GroupName(Integer idGroupName, Character groupName) {
        this.idGroupName = idGroupName;
        this.groupName = groupName;
    }

    public Integer getIdGroupName() {
        return idGroupName;
    }

    public void setIdGroupName(Integer idGroupName) {
        this.idGroupName = idGroupName;
    }

    public Character getGroupName() {
        return groupName;
    }

    public void setGroupName(Character groupName) {
        this.groupName = groupName;
    }

//    @XmlTransient
//    public List<GroupDetails> getGroupDetailsList() {
//        return groupDetailsList;
//    }
//
//    public void setGroupDetailsList(List<GroupDetails> groupDetailsList) {
//        this.groupDetailsList = groupDetailsList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGroupName != null ? idGroupName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupName)) {
            return false;
        }
        GroupName other = (GroupName) object;
        if ((this.idGroupName == null && other.idGroupName != null) || (this.idGroupName != null && !this.idGroupName.equals(other.idGroupName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.GroupName[ idGroupName=" + idGroupName + " ]";
    }

    @Override
    public int compareTo(GroupName o) {
        return Character.compare(this.getGroupName(), o.getGroupName());
    }
    
}
