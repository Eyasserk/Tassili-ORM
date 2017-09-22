/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.FilterColumn;
import com.upspain.tassili.FilterTable;
import com.upspain.tassili.persistence.AbstractFilter;

/**
 *
 * @author ykantour
 */
@FilterTable(DataSourceId = "portugalFr", ReferenceTable = "FPXCARD")
public class TarjetaFilter extends AbstractFilter{
    @FilterColumn(Name = "PAN")
    private String pan;

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
