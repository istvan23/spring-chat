import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components';
import { addGroupRole } from '../../reducers/groupRoleReducer';
import { getMembershipById } from '../../reducers/membershipReducer';
import MemberInfo from './MemberInfo'

const ManagementMembersPage = ({dispatch}) => {

    const members = useSelector(state => state.membership);

    const acceptJoinRequest = (access_token, member) => {
      const privilege = {
        name: "READ_GROUP"
      };
      const role = {
          userId: member.userId,
          name: "ROLE_MEMBER",
          displayName: "Member",
          authorities: [privilege]
      };
      Promise.resolve(dispatch(addGroupRole(access_token, member.chatGroup.id, member.userId, role)))
      .then(() => {
        dispatch(getMembershipById(access_token, member.id, member.chatGroup.id))
      })
      
    }


  return (
    <MembersPageMainContainer>
      <MainContainerTable>
        <tr>
          <td>
            <MemberListContainer>
              {members.map(member => <MemberInfo key={member.id} member={member} dispatch={dispatch} acceptJoinRequest={acceptJoinRequest}/>)}
            </MemberListContainer>
          </td>
          <td>

          </td>
        </tr>
      </MainContainerTable>
        
    </MembersPageMainContainer>
  )
}

const MembersPageMainContainer = styled.div`
  flex: auto;
  background-color: #0f0f0f;
  padding-left: 100px;
  padding-top: 20px;
  padding-bottom: 20px;
  padding-right: 20px;
`;
const MainContainerTable = styled.table`
`;

const MemberListContainer = styled.div`
  height: 600px;
  width: 300px;
  overflow-y: scroll;
`;

const ModifyRoleButton = styled.button`
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

export default ManagementMembersPage