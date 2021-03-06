/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.controller;

import com.pablinchapin.shoca.dao.CategoryDAO;
import com.pablinchapin.shoca.dao.OrderDAO;
import com.pablinchapin.shoca.dao.ProductDAO;
import com.pablinchapin.shoca.entity.Product;
import com.pablinchapin.shoca.form.CustomerForm;
import com.pablinchapin.shoca.model.CartInfo;
import com.pablinchapin.shoca.model.CategoryInfo;
import com.pablinchapin.shoca.model.CustomerInfo;
import com.pablinchapin.shoca.model.ProductInfo;
import com.pablinchapin.shoca.pagination.PaginationResult;
import com.pablinchapin.shoca.utils.Utils;
import com.pablinchapin.shoca.validator.CustomerFormValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pvargas
 */

@Controller
@Transactional
public class MainController {
    
    @Autowired
    private OrderDAO orderDAO;
    
    @Autowired
    private ProductDAO productDAO;
    
    @Autowired
    private CategoryDAO categoryDAO;
    
    @Autowired
    private CustomerFormValidator customerFormValidator;
    
    
    @InitBinder
    public void pcInitBinder(WebDataBinder dataBinder){
        
        Object target = dataBinder.getTarget();
        
        if(target == null){
            return;
        }
        
        System.out.println("Target= " +target);
        
        if(target.getClass() == CartInfo.class){
        
        }else if(target.getClass() == CustomerForm.class){
                System.out.println("target.getClass() == CustomerForm.class" +target.getClass());
                dataBinder.setValidator(customerFormValidator);
        }
    
    }
    
    @RequestMapping("/403")
    public String accessDenied(){
    return "/403";
    }
    
    @RequestMapping("/")
    public String home(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page
    ){
        
        final int maxResult = 5;
        final int maxNavigationPage = 10;
        
        PaginationResult<ProductInfo> result = productDAO.queryProduct(page, maxResult, maxNavigationPage, null, 1);  
        PaginationResult<CategoryInfo> resultCategory = categoryDAO.queryCategory(page, maxResult, maxNavigationPage);
        
        model.addAttribute("paginationResult", result);
        model.addAttribute("paginationResultCategory", resultCategory);
        
    return "index";
    }
    
    
    @RequestMapping({"/categoryList"})
    public String listCategoryHandler(
            Model model,
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "page", defaultValue = "1") int page
    ){
        final int maxResult = 5;
        final int maxNavigationPage = 10;
        
        PaginationResult<CategoryInfo> result = categoryDAO.queryCategories(page, maxResult, maxNavigationPage, likeName);
        
        model.addAttribute("paginationResult", result);
    
    return "categoryList";
    }
    
    @RequestMapping({"/productList"})
    public String listProductHandler(
            Model model,
            @RequestParam(value = "name", defaultValue = "") String likeName,
            @RequestParam(value = "categoryCode", defaultValue = "") String categoryCode,
            @RequestParam(value = "page", defaultValue = "1") int page
    ){
        
        final int sale = 1;
        final int maxResult = 5;
        final int maxNavigationPage = 10;
        
        PaginationResult<ProductInfo> result = productDAO.queryProducts(page, maxResult, maxNavigationPage, likeName, categoryCode, sale);
        PaginationResult<CategoryInfo> resultCategory = categoryDAO.queryCategory(page, maxResult, maxNavigationPage);

        model.addAttribute("paginationResult", result);
        model.addAttribute("paginationResultCategory", resultCategory);
        
            
    return "productList";
    }
    
    
    @RequestMapping({"/productDetail"})
    public String viewProductHandler(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "code", required = true) String code
    ){
        
        Product product = null;
        
        product = productDAO.findProduct(code);
        //ProductInfo result = productDAO.findProductInfo(code);
        
        ProductInfo productInfo = new ProductInfo(product);
        CartInfo cartInfo = Utils.getCartInSession(request);
        //cartInfo.updateQuantity(cartForm);
        
        model.addAttribute("productInfo", productInfo);
        model.addAttribute("cartForm", cartInfo);
    
        return "productDetail";
    }
    
    
    /*
    @RequestMapping({"/buyProduct"})
    public String listProductHandler(
            HttpServletRequest request, 
            Model model,
            @RequestParam(value = "code", defaultValue = "") String code
    ){
        
        Product product = null;
        
        if(code != null && code.length() > 0){
            product = productDAO.findProduct(code);
        }
        
        if(product != null){
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            
            cartInfo.addProduct(productInfo, 1);
        }
    
    return "redirect:/shoppingCart";
    }
    */
    
    /*
    @RequestMapping({"/shoppingCartRemoveProduct"})
    public String removerProductHandler(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "code", defaultValue = "") String code
    ){
        
        Product product = null;
        
        if(code != null && code.length() > 0){
            product = productDAO.findProduct(code);
        }
        
        if(product != null){
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            
            cartInfo.removeProduct(productInfo);
        }
    
    return "redirect:/shoppingCart";
    }
    */
    
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
    public String shoppingCartUpdateQuantity(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            @RequestParam(value = "action", defaultValue = "add") String action
            //@ModelAttribute("cartForm") CartInfo cartForm
    ){
        
        Product product = null;
        
        /*
        System.out.println("/shoppingCart POST -- code-> "+code);
        System.out.println("/shoppingCart POST -- quantity-> "+quantity);
        System.out.println("/shoppingCart POST -- action-> "+action);
        */
        
        if(code != null && code.length() > 0){
            product = productDAO.findProduct(code);
        }
        
        if(product != null){
            
            CartInfo cartInfo = Utils.getCartInSession(request);
            ProductInfo productInfo = new ProductInfo(product);
            
            if(action.equals("add")){
                cartInfo.addProduct(productInfo, quantity);
            }else{
                    cartInfo.removeProduct(productInfo);
            }
            
        }
        
        
        CartInfo cartInfo = Utils.getCartInSession(request);
        //cartInfo.updateQuantity(cartForm);
        model.addAttribute("cartForm", cartInfo);
    
    return "redirect:/shoppingCart";
    }
    
    
    @RequestMapping(value = {"shoppingCart"}, method = RequestMethod.GET)
    public String shoppingCartHandler(
            HttpServletRequest request,
            Model model
    ){
        
        CartInfo newCart = Utils.getCartInSession(request);
        
        model.addAttribute("cartForm", newCart);
    
    return "shoppingCart";
    }
    
    
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(
            HttpServletRequest request,
            Model model            
    ){
        
        CartInfo cartInfo = Utils.getCartInSession(request);
        
        if(cartInfo.isEmpty()){
            return "redirect:/shoppingCart";
        }
        
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        CustomerForm customerForm = new CustomerForm(customerInfo);
        
        model.addAttribute("customerForm", customerForm);
        
    return "shoppingCartCustomer";
    }
    
    
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(
            HttpServletRequest request,
            Model model,
            @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
            BindingResult result,
            final RedirectAttributes redirectAttributes
    ){
        
        if(result.hasErrors()){
            customerForm.setValid(false);
            
            return "shoppingCartCustomer";
        }
        
        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);
    
    return "redirect:/shoppingCartConfirmation";
    }
    
    
    @RequestMapping(value = {"/shoppingCartCheckout"}, method = RequestMethod.GET)
    public String shoppingCartCheckoutReview(
            HttpServletRequest request,
            Model model
    ){
        
        CartInfo cartInfo = Utils.getCartInSession(request);
        
        if(cartInfo == null || cartInfo.isEmpty()){
            return "redirect:/shoppingCart";
        }/*else if(!cartInfo.isValidCustomer()){
                return "redirect:/shoppingCartCustomr";
        }
        */
        
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        CustomerForm customerForm = new CustomerForm(customerInfo);
 
        model.addAttribute("cartForm", cartInfo);
        model.addAttribute("customerForm", customerForm);
        
    
    return "shoppingCartCheckout";
    }
    
    
    @RequestMapping(value = {"/shoppingCartCheckout"}, method = RequestMethod.POST)
    public String shoppingCartCheckoutSave(
            HttpServletRequest request,
            Model model,
            @ModelAttribute("customerForm") @Validated CustomerForm customerForm,
            BindingResult result,
            final RedirectAttributes redirectAttributes
    ){
        
        CartInfo cartInfo = Utils.getCartInSession(request);
        
        if(result.hasErrors()){
            
            for(Object object : result.getAllErrors()) {
                if(object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;

                    System.out.println("/shoppingCartCheckout POST fieldError-> " +fieldError.getCode());
                }

                if(object instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) object;

                    System.out.println("/shoppingCartCheckout POST objectError-> " +objectError.getCode());
                }
            }
            
            customerForm.setValid(false);
            
            model.addAttribute("cartForm", cartInfo);
            model.addAttribute("customerForm", customerForm);
            
            return "shoppingCartCheckout";
        }
        
        customerForm.setValid(true);
        
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);
        
        
        model.addAttribute("cartForm", cartInfo);
        model.addAttribute("customerForm", customerForm);
        
        
        return "redirect:/shoppingCartConfirmation";
    }
    
    
    
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(
            HttpServletRequest request,
            Model model
            ){
        
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        
        
        //System.out.println("@RequestMapping(value = {\"/shoppingCartConfirmation\"}, method = RequestMethod.GET)");
        /*
        if(cartInfo == null || cartInfo.isEmpty()){
            return "redirect:/shoppingCart";
        }else if(!cartInfo.isValidCustomer()){
            return "redirect:/shoppingCartCheckout";
        }
        */
        model.addAttribute("cartForm", cartInfo);
        model.addAttribute("customerForm", customerInfo);
        
        return "shoppingCartConfirmation";
    }
    
    
    @RequestMapping(value = {"shoppingCartConfirmation"}, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(
            HttpServletRequest request, 
            Model model
    ){
        CartInfo cartInfo = Utils.getCartInSession(request);
        /*
        if(cartInfo == null || cartInfo.isEmpty()){
            return "redirect:/shoppingCart";
        }else if(!cartInfo.isValidCustomer()){
            return "redirect:/shoppingCartCheckout";
        }
        */
        try{
            orderDAO.saveOrder(cartInfo);
        }catch(Exception ex){
            //System.out.println("}catch(Exception ex){" +ex.getLocalizedMessage());
            //System.out.println(ex.getMessage());
            return "shoppingCartConfirmation";
        }
        
        Utils.removeCartInsession(request);
        Utils.storeLastOrderedCartInSession(request, cartInfo);
    
        return "redirect:/shoppingCartFinalize";
        //return "redirect:/";
    }
    
    
    
        
    
    @RequestMapping(value = {"/shoppingCartFinalize"}, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model){
    
        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
        
        if(lastOrderedCart == null){
            return "redirect:/shoppingCart";
        }
        
        model.addAttribute("lastOrderedCart", lastOrderedCart);
            
    return "shoppingCartFinalize";
    }
    
    
    @RequestMapping(value = {"/productImage"}, method = RequestMethod.GET)
    public void productImage(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            @RequestParam("code") String code
    ) throws IOException {
        
        Product product = null;
        
        if(code != null){
            product = this.productDAO.findProduct(code);
        }
        
        if(product != null && product.getImage() != null){
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
    
    response.getOutputStream().close();
    }
    
    
}
