import React, { useState } from 'react'
import styled from 'styled-components';

const LoginPage = ({login}) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");


  const changeUsername = (e) => {
    setUsername(e.target.value);
  }
  const changePassword = (e) => {
    setPassword(e.target.value);
  }

  const submitForm = (e) => {
    e.preventDefault();
    login(username, password);
  }


  return (
    <LoginPageContainer>
      <LoginForm onSubmit={submitForm}>
        <p>Username</p> <InputField type="text" name="username" onChange={changeUsername}/>
        <p>Password</p> <InputField type="password" name="password" onChange={changePassword}/>
        <LoginButton>Login</LoginButton>
      </LoginForm>
    </LoginPageContainer>
  )
}

const LoginPageContainer = styled.div`
  background-color: #0f0f0f;
  width: 400px;
  height: 300px;
`;

const LoginForm = styled.form`
  padding: 50px;
`;

const InputField = styled.input`
  width: 200px;
`;

const LoginButton = styled.button`
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


export default LoginPage