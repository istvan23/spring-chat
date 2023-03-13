import React, { useEffect } from 'react'
import { useSelector } from 'react-redux'
import { checkUserRoleToAccess } from '../../helper/UserRoleCheckHelper'
import { createNewAnnouncement, fetchAllAnnouncementOfGroup } from '../../reducers/groupAnnouncementReducer'
import AnnouncementBlock from './AnnouncementBlock'
import NewAnnouncementForm from './NewAnnouncementForm'
import styled from 'styled-components'

const GroupAnnouncementPage = ({dispatch}) => {

  const user = useSelector(state => state.user)
  const appView = useSelector(state => state.appView)
  const announcements = useSelector(state => state.announcement);
  const groupMembership = useSelector(state => state.user.user.chatGroupMemberships.find(membershipElement => membershipElement.chatGroup.id == state.appView.actualGroup));

  useEffect(()=>{
    dispatch(fetchAllAnnouncementOfGroup(user.access_token, appView.actualGroup))
  }, [appView])

  const sendNewAnnouncement = (newAnnouncementTitle, newAnnouncementText) => {
    const announcement = {
      title: newAnnouncementTitle,
      text: newAnnouncementText,
      authorId: user.user.id,
      authorUsername: user.user.username,
      chatGroupId: appView.actualGroup
    }
    dispatch(createNewAnnouncement(user.access_token, announcement));
  }

  return (
    <GroupAnnouncementsMainContainer>
      
      {announcements != null ? announcements.map(element => <AnnouncementBlock announcement={element}/>) : <></>} <br/>
      {checkUserRoleToAccess(groupMembership.role, "GROUP_ADMINISTRATOR") ? <NewAnnouncementForm sendNewAnnouncement={sendNewAnnouncement}/> : null}
    </GroupAnnouncementsMainContainer>
  )
}

const GroupAnnouncementsMainContainer = styled.div`
  flex: auto;
  background-color: #0f0f0f;
  padding-left: 100px;
  padding-top: 20px;
  padding-bottom: 20px;
  padding-right: 20px;
`;

export default GroupAnnouncementPage