/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.dao;

import com.pablinchapin.shoca.entity.Product;
import com.pablinchapin.shoca.form.ProductForm;
import com.pablinchapin.shoca.model.ProductInfo;
import com.pablinchapin.shoca.pagination.PaginationResult;
import java.io.IOException;
import java.util.Date;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pvargas
 */

@Transactional
@Repository
public class ProductDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public Product findProduct(String code){
        try{
            String sql = "SELECT i FROM "+Product.class.getName() + "i WHERE i.CODE = :code ";
            
            Session session = this.sessionFactory.getCurrentSession();
            Query<Product> query = session.createQuery(sql, Product.class);
            query.setParameter("code", code);
            
            return(Product) query.getSingleResult();
        }catch(NoResultException e){
                return null;
        }
    }
    
    public ProductInfo findProductInfo(String code){
        Product product = this.findProduct(code);
        
        if(product == null){
            return null;
        }
        
        return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
    }
    
    
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(ProductForm productForm){
        
        Session session = this.sessionFactory.getCurrentSession();
        
        String code = productForm.getCode();
        
        Product product = null;
        
        boolean isNew = false;
        
        if(code != null){
            product = this.findProduct(code);
        }
        
        if(product == null){
            isNew = true;
            product = new Product();
            product.setCreateDate(new Date());
        }
        
        product.setCode(code);
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        
        if(productForm.getFileData() != null){
            byte[] image = null;
            
            try{
                image = productForm.getFileData().getBytes();
            }catch(IOException e){
                
            }
            
            if(image != null && image.length > 0){
                product.setImage(image);
            }
        }
        
        if(isNew){
            session.persist(product);
        }
        
        session.flush();
    
    }
    
    
    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName){
    
        String sql = "SELECt NEW " +ProductInfo.class.getName()
                +" (p.CODE, p.NAME, p.PRICE )" + " FROM "
                + Product.class.getName() + " p ";
        
        if(likeName != null && likeName.length() > 0){
            sql += " WHERE LOWER(p.NAME) LIKE :likeName ";
        }
        
        sql += " ORDER BY p.CREATED_DATE DESC; ";
        
        Session session = this.sessionFactory.getCurrentSession();
        Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);
        
        if(likeName != null && likeName.length() > 0){
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        
        return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }
    
    
    public PaginationResult<ProductInfo> queryProduct(int page, int maxResult, int maxNavigationPage){
        return queryProducts(page, maxResult, maxNavigationPage, null);
    }
    
    
    
    
    
    
}
