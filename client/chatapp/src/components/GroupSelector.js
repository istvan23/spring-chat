import React, { useEffect } from 'react'
import { useSelector } from 'react-redux'
import styled from 'styled-components'
import { openNewPage, setActualGroupView, toggleSelectorPage } from '../reducers/appViewReducer'
import { refetchUserData, userJoinGroup } from '../reducers/authReducer'
import { createNewChatGroup, eraseGroupState, fetchAllGroup, fetchInformationAboutGroups, getGroupById } from '../reducers/chatGroupReducer'
import { addNewMembership } from '../reducers/membershipReducer'
import { getAllRoomOfGroup } from '../reducers/roomReducer'
import GroupFinderForm from './GroupFinderForm'
import NewGroupFrom from './NewGroupFrom'

const GroupSelector = ({dispatch}) => {

  const user = useSelector(state => state.user)

  const groupsWhereUserHasMembership = user.user.chatGroupMemberships
  const availableGroups = useSelector(state => state.group);
  const selectGroup = (event, group) => {
      const openGroupData = {
        groupId: group.id,
        name: group.name,
        actualRoom: -1,
      }
      
      dispatch(openNewPage(openGroupData));
      dispatch(setActualGroupView(group.id));
      dispatch(getGroupById(user.access_token, group.id));
      dispatch(getAllRoomOfGroup(user.access_token,group.id));
      dispatch(toggleSelectorPage(false))

  }
  const fetchAvailableGroups = (filterText) => {
    dispatch(fetchInformationAboutGroups(user.access_token, filterText));
  }
  const joinGroup = (e, groupId) => {
    const joinForm = {
      userId: user.user.id,
      groupId: groupId
    };
    dispatch(userJoinGroup(user.access_token, joinForm));
  }

  const refreshMembershipList = (e) => {
    dispatch(refetchUserData(user.access_token, user.user.id))
  }


  const createNewGroup = (groupName) => {
      const groupObject = {
        name: groupName,
        groupAdministratorId: user.user.id,
        groupAdministratorUsername: user.user.username
      }

    Promise.resolve(dispatch(createNewChatGroup(user.access_token, groupObject)))
    .then(() => {
      dispatch(refetchUserData(user.access_token, user.user.id));
    })
    
    
  }
  return (
    <GroupSelectorMainContainer>
      <MainContainerTable>
        <tr>
          <td>
            <p>Groups where you have membership:</p>
            
            <GroupListContainer>
              <RefreshButton onClick={refreshMembershipList}>Refresh list</RefreshButton>
              {
                groupsWhereUserHasMembership != undefined ? groupsWhereUserHasMembership.map(membership => (<SelectableGroupContainer key={membership.id} > <SelectGroupButton key={membership.chatGroup.id.toString()} disabled={membership.role == null} onClick={(e) => selectGroup(e, membership.chatGroup)}>{membership.chatGroup.name}{membership.role == null ? "<Applied>" : ""}</SelectGroupButton></SelectableGroupContainer>)) : <></>
              }
            </GroupListContainer>
            <NewGroupFormContainer>
              <NewGroupFrom createNewGroup={createNewGroup}/>
            </NewGroupFormContainer>
            
          </td>
          
          <td>
              <p>Find and try to join a group:</p>
              <GroupListContainer>
                {
                  availableGroups != undefined ? availableGroups.map(group => (<SelectableGroupContainer > <SelectGroupButton key={group.id.toString()} onClick={(e) => joinGroup(e, group.id)}>{group.name}</SelectGroupButton></SelectableGroupContainer>)) : <></>
                }
              </GroupListContainer>
              <NewGroupFormContainer>
                <GroupFinderForm fetchGroups={fetchAvailableGroups}/>
              </NewGroupFormContainer>
          </td>

        </tr>
      </MainContainerTable>

    </GroupSelectorMainContainer>
  )
}

const GroupSelectorMainContainer = styled.div`
  flex: auto;
  background-color: #0f0f0f;
  padding-left: 100px;
  padding-top: 20px;
  padding-bottom: 20px;
  padding-right: 20px;
`;
const MainContainerTable = styled.table`
`;

const GroupListContainer = styled.div`
  height: 600px;
  width: 300px;
  overflow-y: scroll;
`;

const SelectableGroupContainer = styled.div`
`;
const SelectGroupButton = styled.button`
  display: flex;
  flex-direction: column;
  background-color: #1a1919;
  color: antiquewhite;
  padding: 15px 5px 15px 5px;
  border: 0px;
  min-width: 100px;
  width: 80%;
  margin: 10px;
  text-align: left;
  &:hover {
    background-color: #302e2e;
  }
`;

const RefreshButton = styled.button`
  display: flex;
  flex-direction: column;
  background-color: #1a1919;
  color: antiquewhite;
  padding: 15px 5px 15px 5px;
  border: 0px;
  min-width: 100px;
  width: 80%;
  margin: 10px;
  text-align: left;
  &:hover {
    background-color: #302e2e;
  }
`;

const NewGroupFormContainer = styled.div``;

export default GroupSelector