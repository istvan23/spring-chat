import axios from 'axios'

const baseUrl = "http://localhost:8080/api/room";

const getAllRoomOfGroup = async (access_token, groupId) => {
    const config = {
        headers: {
            Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/byGroup/"+groupId, config);
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

const getRoomById = async (access_token, roomId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/"+roomId, config);
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}


const createNewRoom = async (access_token, newRoom) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.post(baseUrl, newRoom, config);
    if(response.status == 200){
        return response.data;
    }
}

const updateRoom = async (access_token, updatedRoom, roomId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.put(baseUrl+"/"+roomId, updatedRoom, config);
    if(response.status == 200){
        return response.data;
    }
}

const deleteRoom = async (access_token, roomId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.delete(baseUrl+"/"+roomId, config);
    if(response.status == 200){
        return "Deleted";
    }
}

export default {getRoomById, getAllRoomOfGroup, createNewRoom, updateRoom, deleteRoom}