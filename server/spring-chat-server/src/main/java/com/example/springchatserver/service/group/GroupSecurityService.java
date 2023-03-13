package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.User;

/**
 * This interface describes the basic methods what need for the GroupSecurityExpressionHandler.
 */
public interface GroupSecurityService {
    /**
     * This method decides that, first of all, whether it has a membership in the given group and it can have access
     * to that group and has the right to make an action.
     *
     * @param user object is hold the information about the authenticated user, who sent the request to the API
     * @param groupId value is the identification key of the ChatGroup, which the user wants to get access
     * @param actionType is a token that indicates what the user wants to do with the resource
     * @return whether the user can access to the resource
     */
    boolean checkUserAuthorityToAccessGroup(User user, Long groupId, String actionType);

    /**
     * @param user object is hold the information about the authenticated user, who sent the request to the API
     * @param membershipId value is the identification key of the ChatGroupMembership, which the user wants to get access
     * @param actionType is a token that indicates what the user wants to do with the resource
     * @return whether the user can access to the resource
     */
    boolean checkUserAuthorityToAccessMembership(User user, Long membershipId, String actionType);

    /**
     * @param user object is hold the information about the authenticated user, who sent the request to the API
     * @param announcementId value is the identification key of the GroupAnnouncement, which the user wants to get access
     * @param actionType is a token that indicates what the user wants to do with the resource
     * @return whether the user can access to the resource
     */
    boolean checkUserAuthorityToAccessAnnouncement(User user, Long announcementId, String actionType);

    /**
     * @param user object is hold the information about the authenticated user, who sent the request to the API
     * @param roomId value is the identification key of the Room, which the user wants to get access
     * @param actionType is a token that indicates what the user wants to do with the resource
     * @return whether the user can access to the resource
     */
    boolean checkUserAuthorityToAccessRoom(User user, Long roomId, String actionType);

    /**
     * @param user object is hold the information about the authenticated user, who sent the request to the API
     * @param messageId value is the identification key of the RoomMessage, which the user wants to get access
     * @param actionType is a token that indicates what the user wants to do with the resource
     * @return whether the user can access to the resource
     */
    boolean checkUserAuthorityToAccessMessage(User user, Long messageId, String actionType);

    /**
     * @param user object is hold the information about the authenticated user, who sent the request to the API
     * @param roleId value is the identification key of the ChatGroupRole, which the user wants to get access
     * @param actionType is a token that indicates what the user wants to do with the resource
     * @return whether the user can access to the resource
     */
    boolean checkUserAuthorityToAccessRole(User user, Long roleId, String actionType);
}
