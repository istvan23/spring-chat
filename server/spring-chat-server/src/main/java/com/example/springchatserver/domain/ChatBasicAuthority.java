package com.example.springchatserver.domain;

public enum ChatBasicAuthority implements ChatAuthority{
    USER("USER_BASIC"), ADMIN("ADMIN_BASIC");

    private final String authorityName;

    ChatBasicAuthority(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String getAuthorityName() {
        return authorityName;
    }
}
