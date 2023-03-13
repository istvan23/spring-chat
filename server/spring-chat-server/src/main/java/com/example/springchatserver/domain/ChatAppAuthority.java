package com.example.springchatserver.domain;

public enum ChatAppAuthority {
    // App basic authorities
    USER("USER_BASIC"), ADMIN("ADMIN_BASIC");

    private final String authorityName;

    ChatAppAuthority(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        return authorityName;
    }
}
