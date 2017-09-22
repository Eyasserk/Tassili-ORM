/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.data;

/**
 *
 * @author ykantour
 */
public class Values<T extends Object> {
    private T main;
    private T aux;
    
    public Values(T main){
        this.main = main;
    }
    
    public Values(T main, T aux){
        this.main = main;
        this.aux = aux;
    }

    /**
     * @return the main
     */
    public T getMain() {
        return main;
    }

    /**
     * @param main the main to set
     */
    public void setMain(T main) {
        this.main = main;
    }

    /**
     * @return the aux
     */
    public T getAux() {
        return aux;
    }

    /**
     * @param aux the aux to set
     */
    public void setAux(T aux) {
        this.aux = aux;
    }
    
    public String getMainValue(){
        if(main instanceof String){
            return "'"+main.toString()+"'";
        }else{
            return main.toString();
        }
    }
    
    public String getAuxValue(){
        if(aux instanceof String){
            return "'"+aux.toString()+"'";
        }else{
            return aux.toString();
        }
    }
}
