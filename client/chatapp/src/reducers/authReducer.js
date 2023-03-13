import {createSlice} from '@reduxjs/toolkit'
import authService from '../services/authService'
import membershipService from '../services/membershipService';

const userSlice = createSlice({
    name: 'user',
    initialState: [],
    reducers: {
        setUser(state, action) {
            return action.payload;
        },
        refreshUser(state, action){
            const newState = {
                access_token: state.access_token,
                user: action.payload
            };
            return newState;
        },
        setResponseMessage(state, action) {
            return action.payload;
        },
        eraseAuthState(state) {
            return [];
        },
        addMembershipToUser(state, action){
            const membershipList = [...state.user.chatGroupMemberships];
            membershipList.push(action.payload);
            const updatedUser = {
                ...state.user,
                chatGroupMemberships: membershipList
            };

            return {
                ...state,
                user: updatedUser
            };
        }
    }
})

export const loginUser = credentials => {
    return async dispatch => {
        const user = await authService.login(credentials);
        dispatch(setUser(user));
    }
}
export const registration = credentials => {
    return async dispatch => {
        const result = await authService.registration(credentials);
        dispatch(setResponseMessage(result));
    }
}

export const refetchUserData = (access_token, userId) => {
    return async dispatch => {
        const userData = await authService.fetchUser(access_token, userId);
        dispatch(refreshUser(userData));
    }
}

export const userJoinGroup = (access_token, joinGroupForm) => {
    return async dispatch => {
        const membership = await membershipService.addNewMemberToGroup(access_token, joinGroupForm);
        dispatch(addMembershipToUser(membership));
    }
}

export const {setUser, setResponseMessage, eraseAuthState, refreshUser, addMembershipToUser} = userSlice.actions
export default userSlice.reducer