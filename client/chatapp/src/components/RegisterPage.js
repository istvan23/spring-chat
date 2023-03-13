import React, { useState } from 'react'
import styled from 'styled-components';

const RegisterPage = ({registration}) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");


  const changeUsername = (e) => {
    setUsername(e.target.value);
  }
  const changePassword = (e) => {
    setPassword(e.target.value);
  }
  const changeEmail = (e) => {
    setEmail(e.target.value);
  }

  const submitForm = (e) => {
    e.preventDefault();
    const _username = username;
    const _password = password;
    const _email = email
    setUsername("");
    setPassword("");
    setEmail("");
    registration(_username, _password, _email);
  }


  return (
    <RegisterPageContainer>
      <RegisterForm onSubmit={submitForm}>
        <p>Username</p> <InputField type="text" name="username" onChange={changeUsername} value={username}/>
        <p>Password</p> <InputField type="password" name="password" onChange={changePassword} value={password}/>
        <p>Email</p> <InputField type="text" name="password" onChange={changeEmail} value={email}/>
        <RegisterButton>Registration</RegisterButton>
      </RegisterForm>
    </RegisterPageContainer>
  )
}

const RegisterPageContainer = styled.div`
  background-color: #0f0f0f;
  width: 400px;
  height: 400px;
`;

const RegisterForm = styled.form`
  padding: 50px;
`;

const InputField = styled.input`
  width: 200px;
`;

const RegisterButton = styled.button`
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

export default RegisterPage