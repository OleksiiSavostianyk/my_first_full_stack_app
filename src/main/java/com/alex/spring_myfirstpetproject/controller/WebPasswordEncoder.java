package com.alex.spring_myfirstpetproject.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class WebPasswordEncoder {
 private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
 public static String encode(String password) {
     return bCryptPasswordEncoder.encode(password);
 }


 public static boolean matches(String rawPassword, String encodedPassword) {
     return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
 }
}
