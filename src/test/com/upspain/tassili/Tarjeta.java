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
@Table(Name = "FPXCARD", DataSourceId = "portugalFr")
public class Tarjeta extends AbstractEntity{
    @Id(AutoIncrement = false)
    @Column(Name = "HLDACCID")
    private long id;
    
    @Column(Name = "EMBOSNAME", Transform = Transform.TRIM)
    private String portador;
    
    @Column(Name = "PAN", Transform = Transform.TRIM)
    private String pan;

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
     * @return the portador
     */
    public String getPortador() {
        return portador;
    }

    /**
     * @param portador the portador to set
     */
    public void setPortador(String portador) {
        this.portador = portador;
    }

    /**
     * @return the pan
     */
    public String getPan() {
        return pan;
    }

    /**
     * @param pan the pan to set
     */
    public void setPan(String pan) {
        this.pan = pan;
    }
}
