import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components'
import { setMessages } from '../reducers/chatMessageReducer'
import { fetchAllAnnouncementOfGroup } from '../reducers/groupAnnouncementReducer'
import { setRooms } from '../reducers/roomReducer'
import GroupAnnouncementPage from './announcement/GroupAnnouncementPage'
import GroupRoomChat from './GroupRoomChat'
import GroupRoomListNav from './GroupRoomListNav'
import GroupManagementPage from './management/GroupManagementPage'

const GroupSubPage = ({dispatch}) => {
  const selectedGroup = useSelector(state => state.group != null ? state.group.find(x => x.id == state.appView.actualGroup) : null)
  const selectedGroupState = useSelector(state => state.appView);

    switch(selectedGroupState.groupSubPageView){
      case 0:
        return (
          <SubPageContainer>
            <GroupRoomListNav dispatch={dispatch}/>
            <GroupRoomChat dispatch={dispatch}/>
          </SubPageContainer>)

      case 1:
        return (
          <SubPageContainer>
            <GroupManagementPage dispatch={dispatch}/>
          </SubPageContainer>
        )
      case 2:
        return (
          <SubPageContainer>
            <GroupAnnouncementPage dispatch={dispatch}/>
          </SubPageContainer>
        )
    }

  
}

const SubPageContainer = styled.div`
  display: flex;
`;

export default GroupSubPage