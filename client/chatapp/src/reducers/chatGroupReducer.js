import {createSlice} from '@reduxjs/toolkit'
import groupService from '../services/groupService';
import membershipService from '../services/membershipService';

const groupSlice = createSlice({
    name: 'group',
    initialState: [],
    reducers: {
        refreshGroup(state, action){
            if(state == [] || !state.some(groupElement => groupElement.id == action.payload.id)){
                return [...state, action.payload]

            }
            else{
                return state.map(group => group.id == action.payload.id ? action.payload : group)
            }
            
        },
        setGroups(state, action){
            return action.payload;
        },
        addNewGroup(state, action){
            return [...state, action.payload]
        },
        removeGroup(state, action){

            return state.filter(element => !(element.id == action.payload.chatGroup.id))
        },
        fillOrRefreshMembershipsOfGroup(state, action){
            const updatableGroup = state.find(group => group.id == action.payload.groupId);
            const updated = {
                ...updatableGroup,
                memberships: action.payload.memberships
            }
            return state.map(group => group.id == action.payload.groupId ? updated : group);
        },
        eraseGroupState(state){
            return [];
        }
    }
})

export const getGroupById = (access_token, groupId) => {
    return async dispatch => {
        const group = await groupService.getGroupById(access_token, groupId);
        dispatch(refreshGroup(group));
    }
}

export const fetchAllGroup = (access_token) => {
    return async dispatch => {
        const groups = await groupService.getAllGroup(access_token);
        dispatch(setGroups(groups));
    }
}

export const fetchInformationAboutGroups = (access_token, filterText) => {
    return async dispatch => {
        const groups = await groupService.getInformationAboutAllAvailableGroupToJoin(access_token, filterText);
        console.log("fetchInformationAboutGroups");
        dispatch(setGroups(groups));
    }
}

export const createNewChatGroup = (access_token, newChatGroup) => {
    return async dispatch => {
        const response = await groupService.createNewChatGroup(access_token, newChatGroup);
        console.log("createNewChatGroup finished");
        console.log(response);

    }
}

export const deleteChatGroup = (access_token, groupId) => {
    return async dispatch => {
        const result = await groupService.deleteGroupById(access_token, groupId);
        if(result == "Success"){
            dispatch(removeGroup(groupId));
        }
    }
}

export const updateChatGroup = (access_token, groupId, updatableGroup) => {
    return async dispatch => {
        const group = await groupService.updateChatGroup(access_token, groupId, updatableGroup);
        dispatch(refreshGroup(group));
    }
}


export const fetchMembersOfGroup = (access_token, groupId) => {
    console.log("fetchMembersOfGroup, ",access_token, groupId);
    return async dispatch => {
        const memberships = await membershipService.getAllMembershipOfGroupById(access_token, groupId);
        
        dispatch(fillOrRefreshMembershipsOfGroup({
            groupId: groupId,
            memberships: memberships
        }));
    }
}

export const {refreshGroup, setGroups, addNewGroup, removeGroup, fillOrRefreshMembershipsOfGroup, eraseGroupState} = groupSlice.actions
export default groupSlice.reducer