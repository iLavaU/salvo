package com.codeoftheweb.salvo.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    public static Map<String, Object> makeMap(String key, Object object){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key,object);
        return dto;
    }
    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
