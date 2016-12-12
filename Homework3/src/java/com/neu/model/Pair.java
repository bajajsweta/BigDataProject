/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.model;

import java.util.ArrayList;

/**
 *
 * @author ruchirapatil
 */
public class Pair {
    
    private Product p1;
    private ArrayList<Product> p2;

    public Pair() {
       // p2=new ArrayList<Product>();
    }
    

    public Product getP1() {
        return p1;
    }

    public void setP1(Product p1) {
        this.p1 = p1;
    }

    public ArrayList<Product> getP2() {
        return p2;
    }

    public void setP2(ArrayList<Product> p2) {
        this.p2 = p2;
    }


    
    
    
    
}
