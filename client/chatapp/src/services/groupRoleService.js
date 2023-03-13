import axios from 'axios'

const baseUrl = "http://localhost:8080/api/groupRole"

const getRoleOfGroupMember = async (access_token, groupId, roleId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(baseUrl+"/"+roleId+"/byGroup/"+groupId, config);
    if(response.status == 200){
        return response.data;
    }
}

const updateRoleOfGroupMember = async (access_token, groupId, role) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.put(baseUrl+"/"+role.id+"/byGroup/"+groupId, role, config);
    if(response.status == 200){
        return response.data;
    }
}


const addGroupRoleToUser = async (access_token, groupId, userId, role) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const url = baseUrl + "/byGroup/" + groupId + "/byUser/" + userId;
    const response = await axios.post(url, role, config);
    if(response.status == 200){
        return response.data;
    }
}

const removeGroupRoleOfMember = async (access_token, groupId, userId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    /*
    const response = await axios.delete(baseUrl+"/"+role.id+"/byGroup/"+groupId, config);
    if(response.status == 200){
        return "Group role successfully deleted";
    }
    */
}

export default {getRoleOfGroupMember,updateRoleOfGroupMember,addGroupRoleToUser,removeGroupRoleOfMember}