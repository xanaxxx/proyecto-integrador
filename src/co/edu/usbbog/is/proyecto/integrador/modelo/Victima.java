/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.usbbog.is.proyecto.integrador.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author orion
 */
@Entity
@Table(name = "victima", catalog = "trata_personas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Victima.findAll", query = "SELECT v FROM Victima v")
    , @NamedQuery(name = "Victima.findById", query = "SELECT v FROM Victima v WHERE v.id = :id")
    , @NamedQuery(name = "Victima.findByNickname", query = "SELECT v FROM Victima v WHERE v.nickname = :nickname")
    , @NamedQuery(name = "Victima.findByNombre", query = "SELECT v FROM Victima v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "Victima.findByClave", query = "SELECT v FROM Victima v WHERE v.clave = :clave")
    , @NamedQuery(name = "Victima.findByCorreo", query = "SELECT v FROM Victima v WHERE v.correo = :correo")})
public class Victima implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;
    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    @Basic(optional = false)
    @Column(name = "clave", nullable = false, length = 50)
    private String clave;
    @Basic(optional = false)
    @Column(name = "correo", nullable = false, length = 50)
    private String correo;
    @OneToMany(mappedBy = "idUsuario")
    private List<Testimonio> testimonioList;

    public Victima() {
    }

    public Victima(Integer id) {
        this.id = id;
    }

    public Victima(Integer id, String nickname, String nombre, String clave, String correo) {
        this.id = id;
        this.nickname = nickname;
        this.nombre = nombre;
        this.clave = clave;
        this.correo = correo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @XmlTransient
    public List<Testimonio> getTestimonioList() {
        return testimonioList;
    }

    public void setTestimonioList(List<Testimonio> testimonioList) {
        this.testimonioList = testimonioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Victima)) {
            return false;
        }
        Victima other = (Victima) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.usbbog.is.proyecto.integrador.modelo.Victima[ id=" + id + " ]";
    }
    
}
