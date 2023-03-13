import {createSlice} from '@reduxjs/toolkit'
import groupRoleService from '../services/groupRoleService';
import groupService from '../services/groupService';
import membershipService from '../services/membershipService';

const groupRoleSlice = createSlice({
    name: 'groupRole',
    initialState: [],
    reducers: {
        refreshGroupRole(state, action){
            return state.map(groupRole => groupRole.id == action.payload.id ? action.payload : groupRole);
        },
        setGroupRole(state, action){
            return action.payload;
        },
        eraseGroupRoleState(state) {
            return [];
        }
    }
})

export const getAndSetGroupRoleById = (access_token, groupId, roleId) => {
    return async dispatch => {
        const role = await groupRoleService.getRoleOfGroupMember(access_token, groupId, roleId);
        dispatch(setGroupRole(role));
    }
}
export const updateGroupRole = (access_token, groupId, role) => {
    return async dispatch => {
        const role = await groupRoleService.updateRoleOfGroupMember(access_token, groupId, role);
        dispatch(refreshGroupRole(role));
    }

}
export const addGroupRole = (access_token, groupId, userId, role) => {
    return async dispatch => {
        const newRole = await groupRoleService.addGroupRoleToUser(access_token, groupId, userId, role);
        dispatch(setGroupRole(newRole));
    }
}
export const {refreshGroupRole, setGroupRole, eraseGroupRoleState} = groupRoleSlice.actions;
export default groupRoleSlice.reducer;