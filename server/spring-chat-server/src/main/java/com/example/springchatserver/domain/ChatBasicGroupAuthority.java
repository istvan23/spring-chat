package com.example.springchatserver.domain;

public enum ChatBasicGroupAuthority implements ChatAuthority{
    READ("READ"), MESSAGE("MESSAGE"),
    DELETEOWN("DELETE_OWN"), DELETEOTHER("DELETE_OTHER"),
    MODIFYOWN("MODIFY_OWN"), MODIFYOTHER("MODIFY_OTHER"),
    CREATEROOM("CREATE_ROOM"), MODIFYROOM("MODIFY_ROOM"),
    DELETEROOM("DELETE_ROOM"),
    KICK("KICK"), GRANTAUTH("GRANT_AUTH"),
    REVOKE("REVOKE_AUTH"), MODAUTH("MOD_AUTH");


    ChatBasicGroupAuthority(String groupAuthorityName) {
        this.groupAuthorityName = groupAuthorityName;
    }

    private String groupAuthorityName;

    @Override
    public String getAuthorityName() {
        return groupAuthorityName;
    }
}
