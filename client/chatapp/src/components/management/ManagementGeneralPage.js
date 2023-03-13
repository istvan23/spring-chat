import React, { useEffect, useState } from 'react'
import { updateChatGroup } from '../../reducers/chatGroupReducer';
import styled from 'styled-components'

const ManagementGeneralPage = ({group, dispatch}) => {
    const [groupName, setGroupName] = useState(group.name);
    const [isEdit, setIsEdit] = useState(false);


    const switchIsEdit = (e) => {
        const currentStatus = isEdit;
        setIsEdit(!currentStatus);
    }

    const changeGroupName = (e) => {
        setGroupName(e.target.value);
    }

    const save = (e) => {
        const newGroupName = groupName;
        dispatch(updateChatGroup(newGroupName))
    }

  return (
    <ManagementGeneralPageContainer>
        Group name: {group.name} <button onClick={switchIsEdit}>Edit</button> <br/>
        {isEdit ? (<div> New name: <input type="text" value={groupName} onChange={changeGroupName}/><button onClick={save}>Save</button></div>) : null}
        Date of creation: {group.dateOfCreation} <br/>
        Administrator: {group.groupAdministratorUsername} <br/>
        <br/>


    </ManagementGeneralPageContainer>
  )
}

const ManagementGeneralPageContainer = styled.div`
    flex: 0.7;
    padding: 5px 10px 5px 10px;
    margin: 10px;
    width: 100%;
    border: 1px solid black;
    border-radius: 5px;
    background-color: #1a1616;
    color: antiquewhite;
`;

export default ManagementGeneralPage