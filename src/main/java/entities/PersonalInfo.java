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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author java
 */
@Entity
@Table(name = "personal_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonalInfo.findAll", query = "SELECT p FROM PersonalInfo p"),
    @NamedQuery(name = "PersonalInfo.findByIdPersonalInfo", query = "SELECT p FROM PersonalInfo p WHERE p.idPersonalInfo = :idPersonalInfo"),
    @NamedQuery(name = "PersonalInfo.findByFirstName", query = "SELECT p FROM PersonalInfo p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "PersonalInfo.findByLastName", query = "SELECT p FROM PersonalInfo p WHERE p.lastName = :lastName"),
    @NamedQuery(name = "PersonalInfo.findByEmail", query = "SELECT p FROM PersonalInfo p WHERE p.email = :email"),
    @NamedQuery(name = "PersonalInfo.findByVersion", query = "SELECT p FROM PersonalInfo p WHERE p.version = :version")})
public class PersonalInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_personal_info")
    private Long idPersonalInfo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "last_name")
    private String lastName;
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @JoinColumn(name = "id_account", referencedColumnName = "id_account")
    @OneToOne(optional = true)
    private Account idAccount;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPersonalInfo")
    private Competitor competitor;

    public PersonalInfo() {
    }

    public PersonalInfo(Long idPersonalInfo) {
        this.idPersonalInfo = idPersonalInfo;
    }

    public PersonalInfo(Long idPersonalInfo, String firstName, String lastName, String email, long version) {
        this.idPersonalInfo = idPersonalInfo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.version = version;
    }

    public Long getIdPersonalInfo() {
        return idPersonalInfo;
    }

    public void setIdPersonalInfo(Long idPersonalInfo) {
        this.idPersonalInfo = idPersonalInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Account getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Account idAccount) {
        this.idAccount = idAccount;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersonalInfo != null ? idPersonalInfo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalInfo)) {
            return false;
        }
        PersonalInfo other = (PersonalInfo) object;
        if ((this.idPersonalInfo == null && other.idPersonalInfo != null) || (this.idPersonalInfo != null && !this.idPersonalInfo.equals(other.idPersonalInfo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PersonalInfo[ idPersonalInfo=" + idPersonalInfo + " ]";
    }
    
}
