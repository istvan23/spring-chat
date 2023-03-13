import {createSlice} from '@reduxjs/toolkit';
import groupRoleService from '../services/groupRoleService';
import membershipService from '../services/membershipService';

const membershipSlice = createSlice({
    name: 'membership',
    initialState: [],
    reducers: {
        setMemberships(state, action){
            return action.payload;
        },
        updateMembership(state, action){
            const indexOfElement = state.indexOf(membership => membership.id == action.payload.id);
            const current_state = state;
            return current_state.map(membership => membership.id == action.payload.id ? action.payload : membership);
        },
        updateRoleOfMembership(state, action){
            let membership = state.find(element => element.id == action.payload.membershipId);
            membership.role = action.payload.groupRole;
            const current_state = state;
            return current_state.map(element => element.id == membership.id ? membership : element);
        },
        addNewMembership(state, action){
            return [...state, action.payload]
        },
        removeMembership(state, action){

            return state.filter(member => member.id != action.payload )
        },
        eraseMembershipState(state) {
            return [];
        }
    }

    
})

export const createNewMembership = (access_token, groupId, membership) => {
    return async dispatch => {
        const membership = await membershipService.addNewMemberToGroup(access_token, groupId, membership);
    }
}

// This method should not exist, because this reducer responsible to represent the selected group's membership management, 
//and its even wrong because the method signature say it want userId as parameter while the method call an getAllMembershipOfGroupById what want groupId as parameter
/*
export const fetchAllGroupMembership = (access_token, userId) => {
    return async dispatch => {
        const memberships = await membershipService.getAllMembershipOfGroupById(access_token, userId)
        dispatch(setMemberships(memberships));
    }
}*/

export const fetchAllGroupMembership = (access_token, groupId) => {
    return async dispatch => {
        const memberships = await membershipService.getAllMembershipOfGroupById(access_token, groupId)
        dispatch(setMemberships(memberships));
    }
}

export const getMembershipById = (access_token, membershipId, groupId) => {
    return async dispatch => {
        const membership = await membershipService.getMembershipById(access_token, membershipId, groupId);
        dispatch(updateMembership(membership));
    }
}

export const deleteMembershipById = (access_token, membershipId) => {
    return async dispatch => {
        const result = await membershipService.deleteMembership(access_token, membershipId);
        dispatch(removeMembership(membershipId));
    }
}

export const updateMembershipById = (access_token, updatableMembership, membershipId) => {
    return async dispatch => {
        const membership = await membershipService.updateMembership(access_token, updatableMembership, membershipId)
        dispatch(updateMembership(membership));
    }
}
export const updateRoleOfMember = (access_token, updatableGroupRole, groupId, membershipId) => {
    return async dispatch => {
        const role = await groupRoleService.updateRoleOfGroupMember(access_token, groupId, updatableGroupRole);
        const extendedRoleInformationObject = {
            membershipId: membershipId,
            groupRole: role
        };
        dispatch(updateRoleOfMembership(extendedRoleInformationObject));
    }
}

export const {updateMembership, setMemberships, addNewMembership, removeMembership, updateRoleOfMembership, eraseMembershipState} = membershipSlice.actions
export default membershipSlice.reducer;