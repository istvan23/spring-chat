import React, { useState } from 'react'
import styled from 'styled-components';

const NewRoomForm = ({createNewRoom}) => {
    const [roomName, setRoomName] = useState("")

    const submitNewRoomData = (e) => {
        e.preventDefault();
        const room = roomName;
        setRoomName("");
        createNewRoom(room);
    }
    const changeRoomName = (e) => {
        setRoomName(e.target.value)
    }
  return (
    <FormContainer>
        <form onSubmit={submitNewRoomData}>
            Room name: <br/> <FormInput type="text" name="roomName" value={roomName} onChange={changeRoomName}/><br/>
            <CreateRoomButton>Create New Room</CreateRoomButton>
        </form>
    </FormContainer>
  )
}

const FormContainer = styled.div`
  background-color: #1a1919;
  padding: 5px;
  min-width: 200px;
  max-width: 250px;
`;

const FormInput = styled.input`
  background-color: #403e3e;
  border: 1px solid black;
  resize: none;
  color: antiquewhite;
`;


const CreateRoomButton = styled.button`
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

export default NewRoomForm