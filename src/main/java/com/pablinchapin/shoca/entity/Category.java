/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author pvargas
 */

@Entity
@Table(name="CATEGORIES")
public class Category implements Serializable{
    
    private static final Long serialVersionUID = 7L;
    
    
    @Id
    @Column(name = "CODE", length = 20, nullable = false)
    private String code;
    
    @Column(name = "NAME", length = 255, nullable = false) 
    private String name;
    
    @Column(name = "DESCRIPTION", length = 255, nullable = false)
    private String description;
    
    @Column(name = "IMAGE_URL", length = 255, nullable = false)
    private String imageUrl;

    public Category() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    
    
    
    
    
}
