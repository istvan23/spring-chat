import React from 'react'
import styled from 'styled-components'

const MessageBlock = (props) => {
  return (
    <MessageContainer>
      <MessageInfoBox>
        <UsernameBox>
          {props.username}
        </UsernameBox>
        <DatetimeBox>
          {props.datetime}
        </DatetimeBox>
      </MessageInfoBox>
      <MessageBox>
        {props.message}
      </MessageBox>
    </MessageContainer>
  )
}

const MessageContainer = styled.div`
    padding: 5px 10px 5px 10px;
    margin: 10px;
    &:hover {
        background-color: wheat;
        color: black;
    }
    border: 1px solid black;
    border-radius: 5px;
    background-color: #1a1616;
    color: antiquewhite;
`;

const MessageInfoBox = styled.div`
  border-bottom: 1px solid gray;
`;

const UsernameBox = styled.div`
  
`;

const DatetimeBox = styled.div`

`;

const MessageBox = styled.div`
`;

export default MessageBlock