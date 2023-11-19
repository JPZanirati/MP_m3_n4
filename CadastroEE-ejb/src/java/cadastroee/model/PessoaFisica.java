/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroee.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JPZanirati
 */
@Entity
@Table(name = "pessoa_fisica", catalog = "LOJA", schema = "LOJA")
@NamedQueries({
    @NamedQuery(name = "PessoaFisica_1.findAll", query = "SELECT p FROM PessoaFisica_1 p"),
    @NamedQuery(name = "PessoaFisica_1.findByIdFisica", query = "SELECT p FROM PessoaFisica_1 p WHERE p.idFisica = :idFisica"),
    @NamedQuery(name = "PessoaFisica_1.findByCpf", query = "SELECT p FROM PessoaFisica_1 p WHERE p.cpf = :cpf"),
    @NamedQuery(name = "PessoaFisica_1.findByDtNasc", query = "SELECT p FROM PessoaFisica_1 p WHERE p.dtNasc = :dtNasc")})
public class PessoaFisica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idFisica")
    private Integer idFisica;
    @Basic(optional = false)
    @Column(name = "cpf")
    private String cpf;
    @Basic(optional = false)
    @Column(name = "dt_nasc")
    @Temporal(TemporalType.DATE)
    private Date dtNasc;
    @JoinColumn(name = "idPessoa", referencedColumnName = "idPessoa")
    @ManyToOne(optional = false)
    private Pessoa idPessoa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFisica")
    private Collection<Movimento> movimentoCollection;

    public PessoaFisica() {
    }

    public PessoaFisica(Integer idFisica) {
        this.idFisica = idFisica;
    }

    public PessoaFisica(Integer idFisica, String cpf, Date dtNasc) {
        this.idFisica = idFisica;
        this.cpf = cpf;
        this.dtNasc = dtNasc;
    }

    public Integer getIdFisica() {
        return idFisica;
    }

    public void setIdFisica(Integer idFisica) {
        this.idFisica = idFisica;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(Date dtNasc) {
        this.dtNasc = dtNasc;
    }

    public Pessoa getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Pessoa idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Collection<Movimento> getMovimentoCollection() {
        return movimentoCollection;
    }

    public void setMovimentoCollection(Collection<Movimento> movimentoCollection) {
        this.movimentoCollection = movimentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFisica != null ? idFisica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PessoaFisica)) {
            return false;
        }
        PessoaFisica other = (PessoaFisica) object;
        if ((this.idFisica == null && other.idFisica != null) || (this.idFisica != null && !this.idFisica.equals(other.idFisica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroee.model.PessoaFisica_1[ idFisica=" + idFisica + " ]";
    }
    
}
