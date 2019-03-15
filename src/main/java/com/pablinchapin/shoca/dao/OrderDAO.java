/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.dao;

import com.pablinchapin.shoca.entity.Order;
import com.pablinchapin.shoca.entity.OrderDetail;
import com.pablinchapin.shoca.entity.Product;
import com.pablinchapin.shoca.model.CartInfo;
import com.pablinchapin.shoca.model.CartLineInfo;
import com.pablinchapin.shoca.model.CustomerInfo;
import com.pablinchapin.shoca.model.OrderDetailInfo;
import com.pablinchapin.shoca.model.OrderInfo;
import com.pablinchapin.shoca.pagination.PaginationResult;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
public class OrderDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired 
    private ProductDAO productDAO;
    
    
    private int getMaxOrderNum(){
    
        String sql = "SELECT MAX(o.orderNum) FROM " +Order.class.getName() + " o ";
        Session session = this.sessionFactory.getCurrentSession();
        
        Query<Integer> query = session.createQuery(sql, Integer.class);
        Integer value = query.getSingleResult();
        
        System.out.println("private int getMaxOrderNum() -> "+ value);
        
        if(value == null){
            return 0;
        }
        
    return value;
    }
    
    
    //@Transactional(rollbackFor = Exception.class)
    public void saveOrder(CartInfo cartInfo){
        
        Session session = this.sessionFactory.getCurrentSession();
        
        int orderNum = this.getMaxOrderNum() + 1;
        
        Order order = new Order();
        
        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());
        
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());
        
        System.out.println("Order "+order.toString());
        
        session.persist(order);
        
        List<CartLineInfo> lines = cartInfo.getCartLines();
        
        
        for(CartLineInfo line : lines){
            
            OrderDetail detail = new OrderDetail();
            
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getProductInfo().getPrice());
            detail.setQuantity(line.getQuantity());
            
            String code = line.getProductInfo().getCode();
            
            Product product = this.productDAO.findProduct(code);
            detail.setProduct(product);
            
            session.persist(detail);
        }
        
        cartInfo.setOrderNum(orderNum);
        
        session.flush();
    }
    
    
    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage){
    
        String sql = "SELECT NEW " +OrderInfo.class.getName()
                + " (o.ID, o.ORDER_DATE, o.ORDER_NUM, o.AMOUNT, "
                + " o.CUSTOMER_NAME, O.CUSTOMER_ADDRESS, o.CUSTOMER_EMAIL, "
                + " o.CUSTOMER_PHONE) FROM "
                + Order.class.getName() 
                + " o ORDER BY o.ORDER_NUM DESC "; 
        
        Session session = this.sessionFactory.getCurrentSession();
        
        Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
        
    return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
        
    }
    
    
    public Order findOrder(String orderId){
        Session session = this.sessionFactory.getCurrentSession();
        
    return session.find(Order.class, orderId);
    }
    
    
    public OrderInfo getOrderInfo(String orderId){
        Order order = this.findOrder(orderId);
        
        if(order == null){
            return null;
        }
        
    return new OrderInfo(
            order.getId(),
            order.getOrderDate(),
            order.getOrderNum(),
            order.getAmount(),
            order.getCustomerName(),
            order.getCustomerAddress(),
            order.getCustomerEmail(),
            order.getCustomerPhone()
    );
    }
    
    
    public List<OrderDetailInfo> listOrderDetailInfo(String orderId){
    
        String sql = "SELECT NEW " +OrderDetailInfo.class.getName() 
                + " (od.ID, od.PRODUCT.CODE, od.PRODUCT.NAME, od.QUANTITY, od.PRICE, od.AMOUNT ) "
                + " FROM "
                + OrderDetail.class.getName() 
                + " od "
                + " WHERE od.ID = :orderId ";
        
        Session session = this.sessionFactory.getCurrentSession();
        Query<OrderDetailInfo> query = session.createNativeQuery(sql, OrderDetailInfo.class);
        
        query.setParameter("orderId", orderId);
        
    return query.getResultList();
    }
    
    
    
}
