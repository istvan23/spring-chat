import axios from 'axios'
import qs from 'qs'

const baseUrl = "http://localhost:8080/api/membership";

const getMembershipById = async (access_token, membershipId, groupId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/byGroup/"+groupId+"/byMembership/"+membershipId, config);
    if(response.status == 200){
        return response.data;
    }
}
const getAllMembershipOfGroupById = async (access_token, groupId) => {
    const config = {
        headers: {
            Authorization: "Bearer " + access_token
        }
    }
    
    const response = await axios.get(baseUrl+"/byGroup/"+groupId, config);
    if(response.status == 200){
        return response.data;
    }
}

const getMembershipsOfUser = async (access_token, userId) => {
    const config = {
        headers: {
            Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/byUser/"+userId, config);
    if(response.status == 200){
        return response.data;
    }
}


    const addNewMemberToGroup = async (access_token, newMemberData) => {
    const config = {
        headers: {
            Authorization: "Bearer " + access_token
        }
    }
    const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded', 'Authorization': 'Bearer ' + access_token },
        data: qs.stringify(newMemberData),
        url: baseUrl,
      };

    const response = await axios.post(baseUrl, newMemberData, config)
    if(response.status == 200){
        return response.data;
    }
}

const updateMembership = async (access_token, updatedMembership, membershipId) => {
    const config = {
        header: {
            Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.put(baseUrl+"/"+membershipId, updatedMembership, config);
    if(response.status == 200){
        return response.data;
    }
}

const deleteMembership = async (access_token, userId, groupId) => {
    const config = {
        header: {
            Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.delete(baseUrl+"/byUser"+userId+"/byGroup/"+groupId, config);
    if(response.status == 200){
        return response.data;
    }
}

export default {getMembershipById, getAllMembershipOfGroupById, getMembershipsOfUser, addNewMemberToGroup, updateMembership, deleteMembership}