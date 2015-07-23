/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_personal_info")
    private Integer idPersonalInfo;
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
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPersonalInfo")
//    private Competitor competitor;
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPersonalInfo")
//    private Account account;

    public PersonalInfo() {
    }

    public PersonalInfo(Integer idPersonalInfo) {
        this.idPersonalInfo = idPersonalInfo;
    }

    public PersonalInfo(Integer idPersonalInfo, String firstName, String lastName, long version) {
        this.idPersonalInfo = idPersonalInfo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.version = version;
    }

    public Integer getIdPersonalInfo() {
        return idPersonalInfo;
    }

    public void setIdPersonalInfo(Integer idPersonalInfo) {
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

//    public Competitor getCompetitor() {
//        return competitor;
//    }
//
//    public void setCompetitor(Competitor competitor) {
//        this.competitor = competitor;
//    }
//
//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.idPersonalInfo);
        hash = 37 * hash + Objects.hashCode(this.firstName);
        hash = 37 * hash + Objects.hashCode(this.lastName);
        hash = 37 * hash + Objects.hashCode(this.email);
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
        final PersonalInfo other = (PersonalInfo) obj;
        if (!Objects.equals(this.idPersonalInfo, other.idPersonalInfo)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "entities.PersonalInfo[ idPersonalInfo=" + idPersonalInfo + " ]";
    }
    
}
