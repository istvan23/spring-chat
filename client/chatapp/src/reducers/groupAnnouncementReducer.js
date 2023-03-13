import {createSlice} from '@reduxjs/toolkit';
import announcementService from '../services/groupAnnouncementService';

const groupAnnouncementSlice = createSlice({
    name: "announcement",
    initialState: [],
    reducers: {
        refreshAnnouncement(state, action){
            return state.map(announcement => announcement.id == action.payload.id ? action.payload : announcement);
        },
        setAnnouncements(state, action){
            return action.payload;
        },
        addNewAnnouncement(state, action){
            return [...state, action.payload]
        },
        removeAnnouncement(state, action){
            return state.filter(announcement => announcement.id != action.payload)
        },
        eraseAnnouncementState(state) {
            return [];
        }
    }
})

export const fetchAllAnnouncementOfGroup = (access_token, groupId) => {
    return async dispatch => {
        const announcements = await announcementService.getAllAnnouncementOfGroup(access_token, groupId);
        dispatch(setAnnouncements(announcements));
    }
}

export const createNewAnnouncement = (access_token, newAnnouncement) => {
    return async dispatch => {
        const announcement = await announcementService.sendNewAnnouncement(access_token, newAnnouncement)
        dispatch(addNewAnnouncement(announcement)); 
    }
}

export const deleteAnnouncement = (access_token, announcementId, groupId) => {
    return async dispatch => {
        const result = await announcementService.deleteAnnouncement(access_token, announcementId, groupId);
        if(result == "Success"){
            dispatch(removeAnnouncement(announcementId));
        }
    }
}

export const updateAnnouncement = (access_token, updatableAnnouncement) => {
    return async dispatch => {
        const announcement = await announcementService.updateAnnouncement(access_token, updatableAnnouncement);
        dispatch(refreshAnnouncement(announcement));
    }
}

export const {refreshAnnouncement, setAnnouncements, addNewAnnouncement, removeAnnouncement, eraseAnnouncementState} = groupAnnouncementSlice.actions;
export default groupAnnouncementSlice.reducer