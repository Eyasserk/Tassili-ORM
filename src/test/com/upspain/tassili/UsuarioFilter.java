/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.FilterColumn;
import com.upspain.tassili.FilterTable;
import com.upspain.tassili.data.Ordering;
import com.upspain.tassili.persistence.AbstractFilter;

/**
 *
 * @author ykantour
 */
@FilterTable(ReferenceTable = "USUARIOS", DataSourceId = "marocAs400")
public class UsuarioFilter extends AbstractFilter{
    @FilterColumn(Name = "ID")
    private long id ;
    
    @FilterColumn(Name = "NOMBRE")
    private String nombre;
    
    @FilterColumn(Name = "EDAD", OrderBy = Ordering.ASC)
    private int edad;
    
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
     * @return the edad
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @param edad the edad to set
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
}
