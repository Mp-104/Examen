package com.example.examen.principal;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MyPrincipal {

    //@Transactional
    public static String getLoggedInUser () {
       String securityContextHolderName = SecurityContextHolder.getContext().getAuthentication().getName();

       return securityContextHolderName;
    }
}
