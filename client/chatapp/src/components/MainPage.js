import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components'
import GroupSelector from './GroupSelector'
import GroupSelectorNavBar from './GroupSelectorNavBar'
import GroupSubPage from './GroupSubPage'
import GroupSubPageSwitchBar from './GroupSubPageSwitchBar'
import HomePage from './HomePage'

const MainPage = ({logout}) => {
  const user = useSelector(state => state.user);
  const showGroupSelectorPage = useSelector(state => state.appView.showGroupSelectorPage);
  const [profileButtonText, setProfileButtonText] = useState("View Profile")
  const dispatch = useDispatch();


  const mouseMoveOnUserInfoButton = (e) => {
    
    setProfileButtonText(user.user.username);
  }
  const mouseMoveOutUserInfoButton = (e) => {
    
    setProfileButtonText("View Profile")
  }

  return (
    <BackgroundContainer>
      { 
        <MainContainer>
            { showGroupSelectorPage == false ? <GroupSubPageSwitchBar dispatch={dispatch}/> : <></>}
            <UserInfoAndGroupSelectorContainer>
                <UserInfoButton onMouseOver={mouseMoveOnUserInfoButton} onMouseOut={mouseMoveOutUserInfoButton}>{profileButtonText}</UserInfoButton><LogoutButton onClick={(e)=>logout()}>Logout</LogoutButton><GroupSelectorNavBar dispatch={dispatch}/>
                
            </UserInfoAndGroupSelectorContainer>
            { showGroupSelectorPage ? <GroupSelector dispatch={dispatch}/> : <GroupSubPage dispatch={dispatch}/>}
        </MainContainer>
      }
      
    </BackgroundContainer>
    
  )
}

const BackgroundContainer = styled.div`
  color: wheat;
`;
/*
const MainContainer = styled.div`
  margin: 1% 5% 0% 5%;
  border: 1px solid rgb(59, 0, 168);
`;
*/

const MainContainer = styled.div`
  margin: 1% 5% 0% 5%;
`;

const UserInfoButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: auto;
  min-width: 80px;
  max-width: 200px;
  text-overflow: clip;
  border: 1px solid black;
  border-top-left-radius: 50px;
  border-bottom-left-radius: 50px;
  margin: 5px 0px 5px 5px;
  &:hover {
    background-color: #2e2828;
  }
`

const LogoutButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: auto;
  max-width: 200px;
  text-overflow: clip;
  border: 1px solid black;
  border-top-right-radius: 50px;
  border-bottom-right-radius: 50px;
  margin: 5px;
  &:hover {
    background-color: #2e2828;
  }
`

const UserInfoAndGroupSelectorContainer = styled.div`
  display: flex;
`;

export default MainPage