import React, { useEffect } from 'react'
import { useSelector } from 'react-redux'
import styled from 'styled-components'
import { setActualGroupView, toggleSelectorPage, updatePageWithActualAppViewState } from '../reducers/appViewReducer'
import { getAllRoomOfGroup } from '../reducers/roomReducer'

const GroupSelectorNavBar = ({dispatch}) => {
  const openedGroups = useSelector(state => state.appView.openedGroupPages)
  const actualGroup = useSelector(state => state.appView.actualGroup)
  const user = useSelector(state => state.user);

  const openGroupSelector = (event) => {
    dispatch(toggleSelectorPage(true))
  }

  const selectGroup = (event, groupId) => {
    dispatch(updatePageWithActualAppViewState())
    dispatch(setActualGroupView(groupId))
    dispatch(getAllRoomOfGroup(user.access_token,groupId))
    dispatch(toggleSelectorPage(false))
  }
  return (
    <NavBarContainer>
      <SwitchToGroupSelectorButton onClick={openGroupSelector}>Open another group</SwitchToGroupSelectorButton>
      {
        openedGroups != null && openedGroups.length > 0 ?
          openedGroups.map( openedGroupState => (<GroupSelectButton key={openedGroupState.groupId} onClick={(e) => selectGroup(e, openedGroupState.groupId)}>{openedGroupState.name}</GroupSelectButton>))
         : <></>
      }
    </NavBarContainer>
  )
}

/*
const NavBarContainer = styled.div`
  background-color: rgb(35, 8, 83);
  padding: 10px;
`;
*/

const NavBarContainer = styled.div`
  background-color: #171616;
  padding: 10px;
  border-bottom: 1px solid #292727;
`;


const GroupSelectButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: 30px;
  min-width: 150px;
  margin-right: 10px;
  border: 1px solid black;
  &:hover {
    background-color: #2e2828;
  }
`;

const SwitchToGroupSelectorButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: 30px;
  margin-right: 10px;
  min-width: 150px;
  border: 1px solid black;
  &:hover {
    background-color: #2e2828;
  }
`;


export default GroupSelectorNavBar