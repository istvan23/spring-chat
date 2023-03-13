import {configureStore} from '@reduxjs/toolkit'
import appViewReducer from './reducers/appViewReducer';
import authReducer from './reducers/authReducer';
import chatGroupReducer from './reducers/chatGroupReducer';
import chatMessageReducer from './reducers/chatMessageReducer';
import groupAnnouncementReducer from './reducers/groupAnnouncementReducer';
import membershipReducer from './reducers/membershipReducer';
import roomReducer from './reducers/roomReducer';

const store = configureStore({
    reducer: {
        user: authReducer,
        group: chatGroupReducer,
        announcement: groupAnnouncementReducer,
        membership: membershipReducer,
        room: roomReducer,
        message: chatMessageReducer,
        appView: appViewReducer
    }
})

export default store;