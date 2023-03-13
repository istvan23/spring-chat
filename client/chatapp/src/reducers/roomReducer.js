import {createSlice} from '@reduxjs/toolkit';
import roomService from '../services/roomService';

const roomSlice = createSlice({
    name: 'room',
    initialState: [],
    reducers: {
        setRooms(state, action){
            return action.payload;
        },
        removeRoom(state, action){

            return state.filter(room => room.id == action.payload)
        },
        addRoom(state, action){
            return [...state, action.payload]
        },
        refreshRoom(state, action){
            return state.map(room => room.id == action.payload.id ? action.payload : room);
        },
        eraseRoomState(state) {
            return [];
        }
    }
});

export const getAllRoomOfGroup = (access_token, groupId) =>{
    return async dispatch => {
        const rooms = await roomService.getAllRoomOfGroup(access_token, groupId);
        dispatch(setRooms(rooms));
    }
}

export const deleteRoom = (access_token, roomId) => {
    return async dispatch => {
        const result = await roomService.deleteRoom(access_token, roomId);
        if(result == "Success"){
            dispatch(removeRoom(roomId));
        }
    }
}

export const createRoom = (access_token, newRoom) => {
    return async dispatch => {
        const room = await roomService.createNewRoom(access_token, newRoom);
        dispatch(addRoom(room));
    }
}

export const updateRoom = (access_token, roomId, updatableRoom) => {
    return async dispatch => {
        const room = await roomService.updateRoom(access_token, updatableRoom, roomId);
        dispatch(refreshRoom(room));
    }
}

export const getRoomById = (access_token, roomId) => {
    return async dispatch => {
        const room = await roomService.getRoomById(access_token, roomId);
        dispatch(refreshRoom(room));
    }
}

export const {setRooms, removeRoom, addRoom, refreshRoom, eraseRoomState} = roomSlice.actions;
export default roomSlice.reducer;