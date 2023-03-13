import React, { useState } from 'react'
import styled from 'styled-components';

const NewGroupFrom = ({createNewGroup}) => {
    const [groupName, setGroupName] = useState("")

    const submitNewGroupData = (e) => {
        e.preventDefault()
        console.log("submitNewGroupData")
        createNewGroup(groupName);
        setGroupName("");
    }
    const changeGroupName = (e) => {
        setGroupName(e.target.value)
    }
  return (
    <div>
        <FormContainer onSubmit={submitNewGroupData}>
            <p>Group name</p> <FormInput type="text" name="groupName" value={groupName} onChange={changeGroupName}/><br/>
            <CreateGroupButton>Create New Group</CreateGroupButton>
        </FormContainer>
    </div>
  )
}

const FormContainer = styled.form`
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


const CreateGroupButton = styled.button`
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

export default NewGroupFrom