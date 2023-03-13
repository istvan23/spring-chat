import axios from 'axios'

const baseUrl = "http://localhost:8080/api/announcement"

const getAllAnnouncementOfGroup = async (access_token, groupId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/byGroup/"+groupId, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

const getAnnouncementByIdOfGroup = async (access_token, announcementId, groupId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/"+announcementId+"/byGroup/"+groupId, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

const sendNewAnnouncement = async (access_token, announcement) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.post(baseUrl, announcement, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

const updateAnnouncement = async (access_token, announcement) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.post(baseUrl, announcement, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}


const deleteAnnouncement = async (access_token, announcementId, groupId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.delete(baseUrl+"/"+announcementId+"/byGroup/"+groupId, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

export default {getAllAnnouncementOfGroup, getAnnouncementByIdOfGroup, sendNewAnnouncement, updateAnnouncement, deleteAnnouncement};