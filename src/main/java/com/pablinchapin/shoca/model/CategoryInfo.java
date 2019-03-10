/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.model;

import com.pablinchapin.shoca.entity.Category;

/**
 *
 * @author pvargas
 */
public class CategoryInfo {
    
    private String code;
    private String name;
    private String description;
    private String imageUrl;

    public CategoryInfo() {
    }

    public CategoryInfo(String code, String name, String description, String imageUrl) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    
    
    public CategoryInfo(Category category){
        this.code = category.getCode();
        this.name = category.getName();
        this.description = category.getDescription();
        this.imageUrl = category.getImageUrl();
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
