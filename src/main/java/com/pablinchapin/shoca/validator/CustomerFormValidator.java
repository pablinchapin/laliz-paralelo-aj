/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pablinchapin.shoca.validator;


import com.pablinchapin.shoca.form.CustomerForm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author pvargas
 */


@Component
public class CustomerFormValidator implements Validator{
    
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CustomerForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        
        System.out.println("public void validate(Object target, Errors errors)");
        
        CustomerForm customerInfo = (CustomerForm) target;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.customerForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.customerForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.customerForm.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.customerForm.phone");
        
        if(!emailValidator.isValid(customerInfo.getEmail())){
            errors.rejectValue("email", "Pattern.customerForm.email");
        }
    }
    
    
    
}
