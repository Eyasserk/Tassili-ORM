/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.com.upspain.tassili;

import com.upspain.tassili.Column;
import com.upspain.tassili.Id;
import com.upspain.tassili.Table;
import com.upspain.tassili.data.DateFormat;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.persistence.AbstractEntity;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;

/**
 *
 * @author ykantour
 */
@Table(Name = "USUARIOS", DataSourceId = "portugalFr")
public class Usuario2 extends AbstractEntity{
    @Id(AutoIncrement = false)
    @Column(Name = "ID")
    private long id;
    
    @Column(Name = "NOMBRE", Transform = Transform.TRIM)
    private String nombre;
    
    @Column(Name = "APELLIDO1", Transform = Transform.TRIM)
    private String apellido1;
    
    @Column(Name = "APELLIDO2", Transform = Transform.TRIM)
    private String apellido2;
    
    @Column(Name = "EDAD")
    private int edad;
    
    @Column(Name = "EMAIL", Transform = Transform.TRIM)
    private String email;
    
    @Column(Name = "PROFESION", Transform = Transform.TRIM)
    private String profesion;
    
    @Column(Name = "FECHANACIMIENTO", Transform = Transform.XMLGREGORIANCALENDAR, Format = DateFormat.DATEFORMAT_2)
    private XMLGregorianCalendar fechaNacimiento;
    
    @Column(Name = "FECHANACIMIENTO2", Transform = Transform.DATETIME, Format = DateFormat.TIMESTAMPFORMAT_4)
    private DateTime fechaNacimiento2;

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
     * @return the apellido1
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * @param apellido1 the apellido1 to set
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * @return the apellido2
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * @param apellido2 the apellido2 to set
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
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

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the profesion
     */
    public String getProfesion() {
        return profesion;
    }

    /**
     * @param profesion the profesion to set
     */
    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    /**
     * @return the fechaNacimiento
     */
    public XMLGregorianCalendar getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * @param fechaNacimiento the fechaNacimiento to set
     */
    public void setFechaNacimiento(XMLGregorianCalendar fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * @return the fechaNacimiento2
     */
    public DateTime getFechaNacimiento2() {
        return fechaNacimiento2;
    }

    /**
     * @param fechaNacimiento2 the fechaNacimiento2 to set
     */
    public void setFechaNacimiento2(DateTime fechaNacimiento2) {
        this.fechaNacimiento2 = fechaNacimiento2;
    }
}
