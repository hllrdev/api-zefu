package com.service.zefu.security;

import java.util.UUID;

public class SecurityConstants {
    
    public static final int JWT_EXPIRATION = 86400000; // 1000 * 60 * 60 * 24 
    public static final String JWT_SECRET = UUID.randomUUID().toString();
    public static final int RECOVERY_EXPIRATION = 900000; //1000 * 60 * 15
    
}
