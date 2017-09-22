/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.context.DataSource;
import com.upspain.tassili.context.DataSourceConfiguration;
import com.upspain.tassili.context.PoolConfiguration;
import com.upspain.tassili.context.TassiliDataSource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author ykantour
 */
public class TassiliBootTest {
    public static void main(String[] args){
        try{
            TassiliDataSource tb = new TassiliDataSource();
            DataSourceConfiguration dsc = new DataSourceConfiguration();
            DataSourceConfiguration dsc2 = new DataSourceConfiguration();

            DataSource ds = new DataSource();
            PoolConfiguration pc = new PoolConfiguration();
            DataSource ds2 = new DataSource();
            PoolConfiguration pc2 = new PoolConfiguration();

            ds.setDBMS("dsds");
            ds.setDriverClass("dsds");
            ds.setPass("sddsd");
            ds.setSchema("sdsds");
            ds.setServer("srvds");
            ds.setUser("usrhfdhd");

            pc.setAquireIncrement("5");
            pc.setMaxIdleTime("4");
            pc.setMaxPoolSize("3");
            pc.setMinPoolSize("2");

            ds2.setDBMS("dsds");
            ds2.setDriverClass("dsds");
            ds2.setPass("sddsd");
            ds2.setSchema("sdsds");
            ds2.setServer("srvds");
            ds2.setUser("usrhfdhd");

            pc2.setAquireIncrement("5");
            pc2.setMaxIdleTime("4");
            pc2.setMaxPoolSize("3");
            pc2.setMinPoolSize("2");

            dsc.setDataSource(ds);
            dsc.setId("primero");
            dsc.setPoolConfiguration(pc);

            dsc2.setDataSource(ds2);
            dsc2.setId("segunda");
            dsc2.setPoolConfiguration(pc2);

            List<DataSourceConfiguration> lista = new ArrayList<DataSourceConfiguration>();
            lista.add(dsc);
            lista.add(dsc2);

            tb.setDataSources(lista);

            JAXBContext jc = JAXBContext.newInstance(TassiliDataSource.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            OutputStream out = new FileOutputStream("C:\\Users/ykantour/Desktop/tassili3.xml");
            marshaller.marshal(tb, out);
        }catch(JAXBException e){
            e.printStackTrace();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
    
}
