/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.Column;
import com.upspain.tassili.Id;
import com.upspain.tassili.Table;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.persistence.AbstractEntity;

/**
 *
 * @author ykantour
 */
@Table(Name = "MATRICULA", DataSourceId = "marocAs400")
public class Matricula extends AbstractEntity{
    
    @Id(AutoIncrement = false)
    @Column(Name = "ID")
    private long id;
    
    @Column(Name = "MATRICULACION", Transform = Transform.TRIM)
    private String matriculacion;

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
     * @return the matriculacion
     */
    public String getMatriculacion() {
        return matriculacion;
    }

    /**
     * @param matriculacion the matriculacion to set
     */
    public void setMatriculacion(String matriculacion) {
        this.matriculacion = matriculacion;
    }
}
