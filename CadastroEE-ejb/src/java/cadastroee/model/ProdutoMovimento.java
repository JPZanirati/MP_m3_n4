/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroee.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author JPZanirati
 */
@Entity
@Table(name = "produto_movimento", catalog = "LOJA", schema = "LOJA")
@NamedQueries({
    @NamedQuery(name = "ProdutoMovimento_1.findAll", query = "SELECT p FROM ProdutoMovimento_1 p"),
    @NamedQuery(name = "ProdutoMovimento_1.findByIdPM", query = "SELECT p FROM ProdutoMovimento_1 p WHERE p.idPM = :idPM")})
public class ProdutoMovimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idPM")
    private Integer idPM;
    @JoinColumn(name = "idMovimento", referencedColumnName = "idMovimento")
    @ManyToOne(optional = false)
    private Movimento idMovimento;
    @JoinColumn(name = "idProduto", referencedColumnName = "idProduto")
    @ManyToOne(optional = false)
    private Produto idProduto;

    public ProdutoMovimento() {
    }

    public ProdutoMovimento(Integer idPM) {
        this.idPM = idPM;
    }

    public Integer getIdPM() {
        return idPM;
    }

    public void setIdPM(Integer idPM) {
        this.idPM = idPM;
    }

    public Movimento getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(Movimento idMovimento) {
        this.idMovimento = idMovimento;
    }

    public Produto getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Produto idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPM != null ? idPM.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdutoMovimento)) {
            return false;
        }
        ProdutoMovimento other = (ProdutoMovimento) object;
        if ((this.idPM == null && other.idPM != null) || (this.idPM != null && !this.idPM.equals(other.idPM))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cadastroee.model.ProdutoMovimento_1[ idPM=" + idPM + " ]";
    }
    
}
