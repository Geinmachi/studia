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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByIdAccount", query = "SELECT a FROM Account a WHERE a.idAccount = :idAccount"),
    @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
    @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password"),
    @NamedQuery(name = "Account.findByIsActive", query = "SELECT a FROM Account a WHERE a.isActive = :isActive"),
    @NamedQuery(name = "Account.findByIsConfirmed", query = "SELECT a FROM Account a WHERE a.isConfirmed = :isConfirmed"),
    @NamedQuery(name = "Account.findByVersion", query = "SELECT a FROM Account a WHERE a.version = :version")})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_account")
    private Integer idAccount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_confirmed")
    private boolean isConfirmed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private long version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAccount", fetch = FetchType.EAGER)
    private List<AccessLevel> accessLevelList = new ArrayList<>();
    @JoinColumn(name = "id_personal_info", referencedColumnName = "id_personal_info")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private PersonalInfo idPersonalInfo;

    public Account() {
    }

    public Account(Integer idAccount) {
        this.idAccount = idAccount;
    }

    public Account(Integer idAccount, String login, String password, boolean isActive, boolean isConfirmed, long version) {
        this.idAccount = idAccount;
        this.login = login;
        this.password = password;
        this.isActive = isActive;
        this.isConfirmed = isConfirmed;
        this.version = version;
    }

    public Integer getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Integer idAccount) {
        this.idAccount = idAccount;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @XmlTransient
    public List<AccessLevel> getAccessLevelList() {
        return accessLevelList;
    }

    public void setAccessLevelList(List<AccessLevel> accessLevelList) {
        this.accessLevelList = accessLevelList;
    }

    public PersonalInfo getIdPersonalInfo() {
        return idPersonalInfo;
    }

    public void setIdPersonalInfo(PersonalInfo idPersonalInfo) {
        this.idPersonalInfo = idPersonalInfo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAccount != null ? idAccount.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.idAccount == null && other.idAccount != null) || (this.idAccount != null && !this.idAccount.equals(other.idAccount))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Account[ idAccount=" + idAccount + " ]";
    }
    
}
