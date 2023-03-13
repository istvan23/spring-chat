import React, { useState } from 'react'
import styled from 'styled-components'

const NewAnnouncementForm = ({sendNewAnnouncement}) => {

  const [announcementTitle, setAnnouncementTitle] = useState("");
  const [announcementText, setAnnouncementText] = useState("");

  const changeAnnouncementText = (e) => {
    setAnnouncementText(e.target.value);
  }
  const changeAnnouncementTitle = (e) => {
    setAnnouncementTitle(e.target.value);
  }

  const submitAnnouncementText = (e) => {
    e.preventDefault();
    sendNewAnnouncement(announcementTitle, announcementText);
    setAnnouncementTitle("");
    setAnnouncementText("");
  }

  return (
    <FormContainer>
      <form onSubmit={submitAnnouncementText}>
        <p>Title: <FormInput type="text" onChange={changeAnnouncementTitle} value={announcementTitle}/></p>
        Text: <FormInputArea type="text" onChange={changeAnnouncementText} value={announcementText}/>
        <NewAnnouncementButton>Send</NewAnnouncementButton>
      </form>
    </FormContainer>
  )
}

const FormContainer = styled.div`
  background-color: #1a1919;
  padding: 5px;
  min-width: 200px;
  max-width: 550px;
`;

const FormInput = styled.input`
  background-color: #403e3e;
  border: 1px solid black;
  width: 100px;
  margin: 5px;
  color: antiquewhite;
`;

const FormInputArea = styled.textarea`
  background-color: #403e3e;
  border: 1px solid black;
  width: 500px;
  margin: 5px;
  height: 50px;
  resize: none;
  color: antiquewhite;
`;


const NewAnnouncementButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: 30px;
  min-width: 40px;
  margin-right: 5px;
  margin-top: 5px;
  border: 1px solid black;
  &:hover {
    background-color: #2e2828;
  }
`;

export default NewAnnouncementForm