package com.example.springchatserver.security;

public class AuthorityNameConverter {
    public static String convert(String groupName, String authority){
        StringBuilder authorityNameBuilder = new StringBuilder();
        authorityNameBuilder.append(groupName.toUpperCase().replaceAll(" ", "_"));
        authorityNameBuilder.append("_").append(authority.toUpperCase());
        return authorityNameBuilder.toString();
    }
}
