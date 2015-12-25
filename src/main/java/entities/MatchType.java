/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author java
 */
@Entity
@Table(name = "match_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatchType.findAll", query = "SELECT m FROM MatchType m"),
    @NamedQuery(name = "MatchType.findByIdMatchType", query = "SELECT m FROM MatchType m WHERE m.idMatchType = :idMatchType"),
    @NamedQuery(name = "MatchType.findByEndUser", query = "SELECT m FROM MatchType m WHERE m.endUser = :endUser"),
    @NamedQuery(name = "MatchType.findBySettable", query = "SELECT m FROM MatchType m WHERE m.settable = TRUE"),
    @NamedQuery(name = "MatchType.findByMatchTypeName", query = "SELECT m FROM MatchType m WHERE m.matchTypeName = :matchTypeName")})
public class MatchType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_match_type")
    private Integer idMatchType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "match_type_name")
    private String matchTypeName;
    @Column(name = "end_user")
    private boolean endUser;
    @Column(name = "settable")
    private boolean settable;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMatchType")
    private List<MatchMatchType> matchMatchTypeList;

    public MatchType() {
    }

    public MatchType(Integer idMatchType) {
        this.idMatchType = idMatchType;
    }

    public MatchType(Integer idMatchType, String matchTypeName) {
        this.idMatchType = idMatchType;
        this.matchTypeName = matchTypeName;
    }

    public Integer getIdMatchType() {
        return idMatchType;
    }

    public void setIdMatchType(Integer idMatchType) {
        this.idMatchType = idMatchType;
    }

    public String getMatchTypeName() {
        return matchTypeName;
    }

    public void setMatchTypeName(String matchTypeName) {
        this.matchTypeName = matchTypeName;
    }

    public boolean isEndUser() {
        return endUser;
    }

    public void setEndUser(boolean endUser) {
        this.endUser = endUser;
    }

    public boolean isSettable() {
        return settable;
    }

    public void setSettable(boolean settable) {
        this.settable = settable;
    }
    
    @XmlTransient
    public List<MatchMatchType> getMatchMatchTypeList() {
        return matchMatchTypeList;
    }

    public void setMatchMatchTypeList(List<MatchMatchType> matchMatchTypeList) {
        this.matchMatchTypeList = matchMatchTypeList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.idMatchType);
        hash = 29 * hash + Objects.hashCode(this.matchTypeName);
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
        final MatchType other = (MatchType) obj;
        if (!Objects.equals(this.idMatchType, other.idMatchType)) {
            return false;
        }
        if (!Objects.equals(this.matchTypeName, other.matchTypeName)) {
            return false;
        }
        return true;
    }

   

    @Override
    public String toString() {
        return "entities.MatchType[ idMatchType=" + idMatchType + " ]";
    }
    
}
