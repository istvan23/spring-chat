package com.example.springchatserver.security;

import com.example.springchatserver.domain.*;
import com.example.springchatserver.service.group.GroupSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.NoSuchElementException;

public class GroupSecurityExpression extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;

    @Autowired
    private GroupSecurityService groupSecurityService;

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     */
    public GroupSecurityExpression(Authentication authentication) {
        super(authentication);
    }

    public boolean hasAuthorityToAccessGroup(Long groupId, String actionType, User user) {
        try {
            return groupSecurityService.checkUserAuthorityToAccessGroup(user, groupId, actionType);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean hasAuthorityToAccessRoom(Long roomId, String actionType, User user) {
        try {
            return this.groupSecurityService.checkUserAuthorityToAccessRoom(user, roomId, actionType);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean hasAuthorityToAccessGroupRole(Long roleId, Long groupId, String actionType, User user) {
        try {
            return this.groupSecurityService.checkUserAuthorityToAccessRole(user, roleId, actionType);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasAuthorityToAccessMessage(Long groupId, Long roomId, Long messageId, String actionType, User user) {
        try {
            return this.groupSecurityService.checkUserAuthorityToAccessMessage(user, messageId, actionType);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean hasAuthorityToAccessGroupAnnouncement(Long groupId, Long announcementId, String actionType, User user) {
        try {
            return this.groupSecurityService.checkUserAuthorityToAccessAnnouncement(user, announcementId, actionType);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean hasAuthorityToAccessUserById(Long id, User user) {
        //final User user = (User) this.authentication.getPrincipal();
        return user.getId().equals(id);
    }

    public boolean hasMembershipById(Long id) {
        final User user = (User) this.authentication.getPrincipal();
        boolean result = user.getChatGroupMemberships().stream().anyMatch(chatGroupMembership -> chatGroupMembership.getId().equals(id));
        return result;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}