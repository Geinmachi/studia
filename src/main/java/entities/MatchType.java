/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
    @NamedQuery(name = "MatchType.findByMatchTypeName", query = "SELECT m FROM MatchType m WHERE m.matchTypeName = :matchTypeName")})
public class MatchType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_match_type")
    private Long idMatchType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "match_type_name")
    private String matchTypeName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMatchType")
    private List<MatchMatchType> matchMatchTypeList;

    public MatchType() {
    }

    public MatchType(Long idMatchType) {
        this.idMatchType = idMatchType;
    }

    public MatchType(Long idMatchType, String matchTypeName) {
        this.idMatchType = idMatchType;
        this.matchTypeName = matchTypeName;
    }

    public Long getIdMatchType() {
        return idMatchType;
    }

    public void setIdMatchType(Long idMatchType) {
        this.idMatchType = idMatchType;
    }

    public String getMatchTypeName() {
        return matchTypeName;
    }

    public void setMatchTypeName(String matchTypeName) {
        this.matchTypeName = matchTypeName;
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
        int hash = 0;
        hash += (idMatchType != null ? idMatchType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatchType)) {
            return false;
        }
        MatchType other = (MatchType) object;
        if ((this.idMatchType == null && other.idMatchType != null) || (this.idMatchType != null && !this.idMatchType.equals(other.idMatchType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MatchType[ idMatchType=" + idMatchType + " ]";
    }
    
}
