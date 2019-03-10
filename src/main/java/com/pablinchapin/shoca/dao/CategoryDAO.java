/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.dao;

import com.pablinchapin.shoca.entity.Category;
import com.pablinchapin.shoca.model.CategoryInfo;
import com.pablinchapin.shoca.pagination.PaginationResult;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pvargas
 */

@Transactional
@Repository
public class CategoryDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
   
    public PaginationResult<CategoryInfo> queryCategories(int page, int maxResult, int maxNavigationPage, String likeName){
    
        String sql = "SELECT NEW "+CategoryInfo.class.getName()
                + " ( c.code, c.name, c.description, c.imageUrl )" + " FROM "
                + Category.class.getName() + " c ";
        
        if(likeName != null && likeName.length() > 0){
            sql += "  WHERE LOWER(c.name) LIKE :likeName ";
        }
        
        sql += " ORDER BY c.code ASC ";
        
        Session session = this.sessionFactory.getCurrentSession();
        Query<CategoryInfo> query = session.createQuery(sql, CategoryInfo.class);
        
        if(likeName != null && likeName.length() > 0){
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        
        return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
    
    }
    
    
    public PaginationResult<CategoryInfo> queryCategory(int page, int maxResult, int maxNavigationPage){
        return queryCategories(page, maxResult, maxNavigationPage, null);
    }
    
}
