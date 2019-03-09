/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.dao;

import com.pablinchapin.shoca.entity.Account;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pvargas
 */

@Transactional
@Repository
public class AccountDAO {
    
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public Account findAccount(String userName){
        Session session = this.sessionFactory.getCurrentSession();
        
    return session.find(Account.class, userName);
    }
    
}
