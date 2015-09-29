/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author java
 */
@Entity
@Table(name = "access_level")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "access_level")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessLevel.findAll", query = "SELECT a FROM AccessLevel a"),
    @NamedQuery(name = "AccessLevel.findByIdAccessLevel", query = "SELECT a FROM AccessLevel a WHERE a.idAccessLevel = :idAccessLevel"),
    @NamedQuery(name = "AccessLevel.findByAccessLevel", query = "SELECT a FROM AccessLevel a WHERE a.accessLevel = :accessLevel"),
    @NamedQuery(name = "AccessLevel.findByIsActive", query = "SELECT a FROM AccessLevel a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "AccessLevel.findByVersion", query = "SELECT a FROM AccessLevel a WHERE a.version = :version")})
public class AccessLevel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_access_level")
    private Integer idAccessLevel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "access_level")
    private String accessLevel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOrganizer")
    private List<Competition> competitionList = new ArrayList<>();
    @JoinColumn(name = "id_account", referencedColumnName = "id_account")
    @ManyToOne(optional = false)
    private Account idAccount;

    public AccessLevel() {
    }

    public AccessLevel(Integer idAccessLevel) {
        this.idAccessLevel = idAccessLevel;
    }

    public AccessLevel(Integer idAccessLevel, String accessLevel, boolean isActive, long version) {
        this.idAccessLevel = idAccessLevel;
        this.accessLevel = accessLevel;
        this.isActive = isActive;
        this.version = version;
    }

    public Integer getIdAccessLevel() {
        return idAccessLevel;
    }

    public void setIdAccessLevel(Integer idAccessLevel) {
        this.idAccessLevel = idAccessLevel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @XmlTransient
    public List<Competition> getCompetitionList() {
        return competitionList;
    }

    public void setCompetitionList(List<Competition> competitionList) {
        this.competitionList = competitionList;
    }

    public Account getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Account idAccount) {
        this.idAccount = idAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAccessLevel != null ? idAccessLevel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessLevel)) {
            return false;
        }
        AccessLevel other = (AccessLevel) object;
        if ((this.idAccessLevel == null && other.idAccessLevel != null) || (this.idAccessLevel != null && !this.idAccessLevel.equals(other.idAccessLevel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccessLevel[ idAccessLevel=" + idAccessLevel + " ]";
    }
    
}
