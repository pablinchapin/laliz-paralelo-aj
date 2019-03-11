/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pvargas
 */

@Entity
@Table(name = "PRODUCTS")
public class Product implements Serializable{
    
    private static final Long serialVersionUID = 1L;
    
    @Id
    @Column(name = "CODE", length = 20, nullable = false)
    private String code;
    
    @Column(name = "CATEGORY_CODE", length = 20, nullable = false)
    private String category_code;
    
    @Column(name = "NAME", length = 255, nullable = false)
    private String name;
    
    @Column(name = "IMAGE_URL", length = 255, nullable = false)
    private String image_url;
    
    @Column(name = "SALE", length = 1, nullable = false)
    private int sale;    
    
    @Column(name = "PRICE", nullable = false)
    private double price;
    
    @Lob
    @Column(name = "IMAGE", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;

    public Product() {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
