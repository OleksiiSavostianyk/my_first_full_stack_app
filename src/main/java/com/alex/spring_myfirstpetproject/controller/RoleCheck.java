package com.alex.spring_myfirstpetproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import javax.management.relation.Role;

public class RoleCheck {
    public static boolean checkAccess(Authentication  authentication, String requiredRole ) {
       String userRole = authentication.getAuthorities().toString().replace("[","").replace("]","");
       if(userRole.equals(requiredRole)) {
           return true;
       }
       return false;
    }
}
