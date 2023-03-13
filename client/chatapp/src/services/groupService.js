import axios from 'axios'
import qs from 'qs'

const baseUrl = "http://localhost:8080/api/group"

// Get ALL
const getAllGroup = async (access_token) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

const getInformationAboutAllAvailableGroupToJoin = async (access_token, filterText) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const url = baseUrl + "/basicInformation" + (filterText != null && filterText != "" ? "/byName/" + filterText : "");
    const response = await axios.get(url, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

// Get ALL membership of a user
const getAllMembershipOfUser = async (access_token, userId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/byUser/"+userId, config)
    if(response.status == 200){
        return response.data;
    }
    else{
        return "Access denied.";
    }
}

// GET
const getGroupById = async (access_token, id) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/"+id, config);
    if(response.status == 200){
        return response.data;
    }
    else if(response.status == 401){
        return "Please authenticate yourself before try to access a resource.";
    }
    else if(response.state == 403){
        return "You do not have authority to access the requested resource."
    }
    else{
        return "The group with the given id is do not exist or an unexpected error occured."
    }
}

// POST
const createNewChatGroup = async(access_token, newChatGroup) => {
    console.log(newChatGroup)
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded', 'Authorization': 'Bearer ' + access_token },
        data: qs.stringify(newChatGroup),
        url: baseUrl,
      };
    const response = await axios.post(baseUrl, newChatGroup, config);

    return response.status;
}

//PUT
const updateChatGroup = async(access_token, groupId, chatGroup) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.put(baseUrl+"/"+groupId, chatGroup, config);
    if(response.status == 200){
        return response.data;
    }
}
// DELETE
const deleteGroupById = async (access_token, id) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.delete(baseUrl+"/"+id, config);
    if(response.status == 200){
        return "Chat group deleted successfully."
    }
    else if(response.status == 401){
        return "Please authenticate yourself before try to access a resource.";
    }
    else if(response.state == 403){
        return "You do not have authority to access the requested resource."
    }
    else{
        return "The group with the given id is do not exist or an unexpected error occured."
    }
}

export default {getAllGroup, getAllMembershipOfUser, getGroupById, createNewChatGroup, updateChatGroup, deleteGroupById, getInformationAboutAllAvailableGroupToJoin}