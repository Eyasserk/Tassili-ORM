/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.Column;
import com.upspain.tassili.Id;
import com.upspain.tassili.Property;
import com.upspain.tassili.Table;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.persistence.AbstractEntity;

/**
 *
 * @author ykantour
 */
@Table(Name = "COCHES", DataSourceId = "marocAs400")
public class Coche extends AbstractEntity{
    @Id(AutoIncrement = false)
    @Column(Name = "ID")
    private long id;
    
    @Column(Name = "NOMBRE", Transform = Transform.TRIM)
    private String nombre;
    
    @Column(Name = "MARCA", Transform = Transform.TRIM)
    private String marca;
    
    @Property(Target = "IDCOCHE")
    private Matricula matricula;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * @return the matricula
     */
    public Matricula getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }
    
}
