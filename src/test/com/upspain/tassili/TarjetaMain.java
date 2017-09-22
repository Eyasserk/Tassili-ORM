/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.persistence.Service;
import java.util.List;

/**
 *
 * @author ykantour
 */
public class TarjetaMain {
    public static void main(String[] args){
        try{
            UsuarioFilter filter = new UsuarioFilter();
            filter.setPagination(false);
            filter.setPageSize(10);
            filter.setPageNumber(0);
            filter.setNombre("Yasser");
            List<Usuario> u = Service.newInstance(Usuario.class).findAll(filter);
            
            System.out.println(u);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
