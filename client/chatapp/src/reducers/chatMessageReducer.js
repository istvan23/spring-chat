import {createSlice} from '@reduxjs/toolkit';
import chatMessageService from '../services/chatMessageService';

const messageSlice = createSlice({
    name: 'message',
    initialState: [],
    reducers: {
        setMessages(state, action){
            return action.payload
        },
        refreshMessage(state, action){
            return state.map(message => message.id == action.payload.id ? action.payload : message);
        },
        removeMessage(state, action){

            return state.filter(message => message.id == action.payload)
        },
        addMessage(state, action){

            return [...state, action.payload]
        },
        eraseMessageState(state) {
            return [];
        }
    }
});

export const fetchAllMessageOfRoom = (access_token, groupId, roomId) => {
    return async dispatch => {
        const messages = await chatMessageService.getAllMessageOfRoom(access_token, groupId, roomId);
        dispatch(setMessages(messages));
    }
}

export const getMessageById = (access_token, messageId) => {
    return async dispatch => {
        const message = await chatMessageService.getMessageById(access_token, messageId);
        dispatch(refreshMessage(message));
    }
}
export const updateMessage = (access_token, messageId, updatableMessage) => {
    return async dispatch => {
        const message = await chatMessageService.updateMessage(access_token, messageId, updatableMessage);
        dispatch(refreshMessage(message));
    }
}

export const deleteMessage = (access_token, messageId) => {
    return async dispatch => {
        const result = await chatMessageService.deleteMessage(access_token, messageId);
        if(result == "success"){
            dispatch(removeMessage(messageId));
        }
    }
}

export const sendNewMessage = (access_token, newMessage) => {
    return async dispatch => {
        const message = await chatMessageService.createNewMessage(access_token, newMessage);
        dispatch(addMessage(message));
    }
}

export const {setMessages, refreshMessage, removeMessage, addMessage, eraseMessageState} = messageSlice.actions
export default messageSlice.reducer;