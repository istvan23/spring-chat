import React from 'react'
import styled from 'styled-components'
import { setGroupSubPageView } from '../reducers/appViewReducer'

const GroupSubPageSwitchBar = ({dispatch}) => {

    const switchSubPage = (e, value) => {
        dispatch(setGroupSubPageView(value));
    }

  return (
    <NavBarContainer>
        <SwitchButton onClick={(e) => switchSubPage(e, 0)}>Chat</SwitchButton>
        <SwitchButton onClick={(e) => switchSubPage(e, 1)}>Informations</SwitchButton>
        <SwitchButton onClick={(e) => switchSubPage(e, 2)}>Announcements</SwitchButton>
    </NavBarContainer>
  )
}

const SwitchButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: 30px;
  min-width: 40px;
  margin-right: 5px;
  border: 1px solid black;
  &:hover {
    background-color: #2e2828;
  }
`;

const NavBarContainer = styled.div`
  background-color: #171616;
  padding: 10px;
  width: max-content;
  border-bottom: 1px solid #292727;
`;

export default GroupSubPageSwitchBar