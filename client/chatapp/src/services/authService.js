import axios from 'axios'
import qs from 'qs'

const baseUrl = 'http://localhost:8080/api'
const loginUrl = baseUrl + "/auth/login";
const userUrl = baseUrl + "/user"
const registrationUrl = userUrl + "/registration";


const login = async (credentials) => {
    const credentialsJson = {
        "username" : credentials.username,
        "password" : credentials.password
    };

    const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        data: qs.stringify(credentialsJson),
        url: loginUrl,
      };


    const response = await axios.post(loginUrl, credentialsJson);

   if(response.status == 200){
        const authenticatedUser = {
            "user" : response.data,
            "access_token" : response.headers['authorization']
        }
        return authenticatedUser;
   }
   return { message: "Authentication failed"};
}

const registration = async (credentials) => {
    console.log("registration request with username=",credentials.username," password=",credentials.password," and ",credentials.email)
    const credentialsJson = {
        "username" : credentials.username,
        "password" : credentials.password,
        "email" : credentials.email
    };
    const options = {
        method: 'POST',
        headers: { 'content-type': 'application/x-www-form-urlencoded' },
        data: qs.stringify(credentialsJson),
        url: registrationUrl,
      };
    const response = await axios.post(registrationUrl, credentialsJson);

    if(response.status == 200){
        return {message: "Registration successful"};
    }
    return {message: "Registration failed"};
}

const fetchUser = async (access_token, userId) => {
    const config = {
        headers: {
           Authorization: "Bearer " + access_token
        }
    }
    const response = await axios.get(userUrl+"/"+userId, config);
    if(response.status == 200){
        return response.data;
    }
}

export default { login, registration, fetchUser}