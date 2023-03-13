import React from 'react'
import styled from 'styled-components';

const GroupManagementSideBar = ({selectOption}) => {

    const selectOptionClick = (e, option) => {
        selectOption(option);
    }

  return (
    <SideBarContainer>
        <SideBarOptionSelectorButton onClick={(e) => selectOptionClick(e, "GENERAL")}>General</SideBarOptionSelectorButton>
        <SideBarOptionSelectorButton onClick={(e) => selectOptionClick(e, "MEMBERS")}>Members</SideBarOptionSelectorButton>
    </SideBarContainer>
  )
}

const SideBarContainer = styled.div`
  flex: 0.3;
  background-color: #1a1919;
  padding: 5px;
  min-width: 100px;
  max-width: 250px;
  height: 800px;
  overflow-y: scroll;
`;

const SideBarOptionSelectorButton = styled.button`
  display: flex;
  flex-direction: column;
  background-color: #1a1919;
  color: antiquewhite;
  padding: 15px 5px 15px 5px;
  border: 0px;
  width: 100%;
  text-align: left;
  &:hover {
    background-color: #302e2e;
  }
`;

export default GroupManagementSideBar