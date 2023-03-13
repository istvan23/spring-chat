import React, { useState } from 'react'
import { useDispatch } from 'react-redux';
import styled from 'styled-components';
import { loginUser, registration } from '../reducers/authReducer';
import LoginPage from './LoginPage';
import RegisterPage from './RegisterPage';

const HomePage = ({message, userDispatch}) => {
    const [showRegisterPage, setShowRegisterPage] = useState(false);


    const toggleLoginOrRegisterPage = (e) => {
        setShowRegisterPage(!showRegisterPage);
    }
    const login = (username, password) =>{
        userDispatch(loginUser({username, password}))
    }
    const reg = (username, password, email) =>{
        userDispatch(registration({username,password,email}));
    }
  return (
    <SignUpContainer>
        <h3>{message != undefined ? message : ""}</h3>
        <HeaderContainer>{!showRegisterPage ? "Login" : "Registration"}</HeaderContainer>
        {showRegisterPage ? <RegisterPage registration={reg}/> : <LoginPage login={login}/>}
        <SwitchButton onClick={toggleLoginOrRegisterPage}>Back to {showRegisterPage ? "Login" : "Registration"} Page</SwitchButton> 
    </SignUpContainer>
  )
}

const SignUpContainer = styled.div`
margin: 10% 10% 10% 10%;
    color: wheat;
`;

const HeaderContainer = styled.div`
    border-top: 1px solid black;
    border-right: 1px solid black;
    border-left: 1px solid black;
    width: 150px;
    text-align: center;
    padding: 10px;
    border-top-left-radius: 20px;
    border-top-right-radius: 20px;
    background-color: #171616;
`;

const SwitchButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: 50px;
  min-width: 100px;
  margin-top: 10px;
  border: 1px solid black;
  &:hover {
    background-color: #2e2828;
  }
`;

export default HomePage