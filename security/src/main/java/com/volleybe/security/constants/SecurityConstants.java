package com.volleybe.security.constants;

public class SecurityConstants {

  public static final String SECRET = "SECRET_KEY";
  public static final long EXPIRATION_TIME = 1L * 2L * 60 * 60L * 1000L;  // 2 hours
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String SIGN_UP_URL = "/api";
}