/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.model;

import com.pablinchapin.shoca.entity.Product;

/**
 *
 * @author pvargas
 */
public class ProductInfo {
    
    private String code;
    private String category_code;
    private String image_url;
    private int sale;
    private String name;
    private double price;

    public ProductInfo() {
    }

    public ProductInfo(String code, String category_code, String image_url, int sale, String name, double price) {
        this.code = code;
        this.category_code = category_code;
        this.image_url = image_url;
        this.sale = sale;
        this.name = name;
        this.price = price;
    }
    
    //For JPA/Hibernate query
    public ProductInfo(Product product) {
        this.code = product.getCode();
        this.category_code = product.getCategory_code();
        this.image_url = product.getImage_url();
        this.sale = product.getSale();
        this.name = product.getName();
        this.price = product.getPrice();
        
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }
    
    
    
    
    
}
