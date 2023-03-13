import React, { useEffect, useState } from 'react'
import GroupManagementSideBar from './GroupManagementSideBar';
import styled from 'styled-components';
import ManagementGeneralPage from './ManagementGeneralPage';
import ManagementMembersPage from './ManagementMembersPage';
import { useSelector } from 'react-redux';
import { fetchAllGroupMembership } from '../../reducers/membershipReducer';

const GroupManagementPage = ({dispatch}) => {
    const group = useSelector(state => state.group.find(element => element.id == state.appView.actualGroup));
    const user = useSelector(state => state.user);
    const [pageOption, setPageOption] = useState("GENERAL");
    const selectOption = (option) => {
      setPageOption(option);
    }

    useEffect(() => {
      dispatch(fetchAllGroupMembership(user.access_token, group.id));
    }, [group])
    
    const subPageContent = () => {
      switch(pageOption){
        case "GENERAL":
          return <ManagementGeneralPage dispatch={dispatch} group={group}/>
        case "MEMBERS":
          return <ManagementMembersPage dispatch={dispatch}/>
      }

    }
  return (
    <ManagementSubPageContainer>
        <GroupManagementSideBar selectOption={selectOption}/>
        {subPageContent()}
    </ManagementSubPageContainer>
  )

}

const ManagementSubPageContainer = styled.div`
  display: flex;
  width: 100%;
`;

export default GroupManagementPage