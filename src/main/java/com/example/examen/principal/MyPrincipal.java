package com.example.examen.principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MyPrincipal {

    public static String getLoggedInUser () {
       String securityContextHolderName = SecurityContextHolder.getContext().getAuthentication().getName();

       return securityContextHolderName;
    }
}
