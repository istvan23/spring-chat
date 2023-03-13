import axios from 'axios'

const baseUrl = "http://localhost:8080/api/message";

const getMessageById = async (access_token, messageId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/"+messageId, config);
    if(response.status == 200){
        return response.data;
    }
}

const getAllMessageOfRoom = async (access_token, groupId, roomId) => {
    const config = {
        headers: {
            Authorization: "Bearer " + access_token
        }
    }

    const response = await axios.get(baseUrl+"/byGroup/"+groupId+"/byRoom/"+roomId, config);
    if(response.status == 200){
        return response.data;
    }
}

const createNewMessage = async (access_token, message) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.post(baseUrl, message, config);
    if(response.status == 200){
        return response.data;
    }
}

const updateMessage = async (access_token, messageId, updatedMessage) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.put(baseUrl+"/"+messageId, updateMessage,config);
    if(response.status == 200){
        return response.data;
    }
}

const deleteMessage = async (access_token, messageId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.delete(baseUrl+"/"+messageId, config);
    if(response.status == 200){
        return response.data;
    }
}

export default {getMessageById, getAllMessageOfRoom, createNewMessage, updateMessage, deleteMessage};