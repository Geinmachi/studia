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
@Table(name = "competition_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompetitionType.findAll", query = "SELECT c FROM CompetitionType c"),
    @NamedQuery(name = "CompetitionType.findByIdCompetitionType", query = "SELECT c FROM CompetitionType c WHERE c.idCompetitionType = :idCompetitionType"),
    @NamedQuery(name = "CompetitionType.findByCompetitionTypeName", query = "SELECT c FROM CompetitionType c WHERE c.competitionTypeName = :competitionTypeName")})
public class CompetitionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competition_type")
    private Integer idCompetitionType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "competition_type_name")
    private String competitionTypeName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompetitionType")
    private List<Competition> competitionList;

    public CompetitionType() {
    }

    public CompetitionType(Integer idCompetitionType) {
        this.idCompetitionType = idCompetitionType;
    }

    public CompetitionType(Integer idCompetitionType, String competitionTypeName) {
        this.idCompetitionType = idCompetitionType;
        this.competitionTypeName = competitionTypeName;
    }

    public Integer getIdCompetitionType() {
        return idCompetitionType;
    }

    public void setIdCompetitionType(Integer idCompetitionType) {
        this.idCompetitionType = idCompetitionType;
    }

    public String getCompetitionTypeName() {
        return competitionTypeName;
    }

    public void setCompetitionTypeName(String competitionTypeName) {
        this.competitionTypeName = competitionTypeName;
    }

    @XmlTransient
    public List<Competition> getCompetitionList() {
        return competitionList;
    }

    public void setCompetitionList(List<Competition> competitionList) {
        this.competitionList = competitionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompetitionType != null ? idCompetitionType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompetitionType)) {
            return false;
        }
        CompetitionType other = (CompetitionType) object;
        if ((this.idCompetitionType == null && other.idCompetitionType != null) || (this.idCompetitionType != null && !this.idCompetitionType.equals(other.idCompetitionType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CompetitionType[ idCompetitionType=" + idCompetitionType + " ]";
    }
    
}
