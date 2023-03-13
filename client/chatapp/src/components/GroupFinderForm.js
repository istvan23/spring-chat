import React, { useState } from 'react'
import styled from 'styled-components'

const GroupFinderForm = ({fetchGroups}) => {
  const [text, setText] = useState("")

  const changeText = (e) => {
    setText(e.target.value);
  }

  const submitFilterTextToFetchGroups = (e) => {
    e.preventDefault();
    fetchGroups(text);
  }
  return (
    <div>
        <FormContainer onSubmit={submitFilterTextToFetchGroups}>
            <p>Group name</p> <FormInput type="text" name="groupName" value={text} onChange={changeText}/><br/>
            <FindGroupButton>Fetch groups</FindGroupButton>
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


const FindGroupButton = styled.button`
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

export default GroupFinderForm