
export const checkUserRoleToAccess = (role, required_permission) => {
    const hasAccess = role.authorities.some(authority => authority.name === required_permission || authority.name === "GROUP_ADMINISTRATOR");
    return hasAccess;
}