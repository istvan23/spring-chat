import React, { useEffect } from 'react'
import { useSelector } from 'react-redux';
import styled from 'styled-components'
import { fetchAllMessageOfRoom, sendNewMessage } from '../reducers/chatMessageReducer';
import ChatInput from './ChatInput';
import MessageBlock from './MessageBlock';

const GroupRoomChat = ({dispatch}) => {
  const messages = useSelector(state => state.message);
  const user = useSelector(state => state.user);
  const currentGroup = useSelector(state => state.appView.actualGroup)
  const currentRoom = useSelector(state => state.appView.actualRoom)

  useEffect(()=> {
    const interval = setInterval(() => {
      if(currentGroup != -1 && currentRoom != -1){
        dispatch(fetchAllMessageOfRoom(user.access_token, currentGroup, currentRoom));
      }
    }, 500);
    return () => clearInterval(interval);
    
  }, [currentRoom])
  const sendMessage = (text) => {
    
    const messageData = {
      groupId: currentGroup,
      roomId: currentRoom, 
      senderId: user.user.id,
      senderName: user.user.username,
      message: text
    }
    dispatch(sendNewMessage(user.access_token, messageData));
  }
  if(currentRoom != -1 && messages != undefined && messages != []){
    return (
      <ChatContainer>
        <MessageListBox>
          {messages.map(messageElement => (<MessageBlock username={messageElement.senderUsername} message={messageElement.message} datetime={messageElement.dateOfMessage}/>)) }
        </MessageListBox>
        <ChatInput sendMessage={sendMessage}/>
      </ChatContainer>
    )
  }
  else{
    return (
      <ChatContainer>
        <MessageListBox>
          <p>No room selected</p>
        </MessageListBox>
      </ChatContainer>
    )
  }
  
}

const MessageListBox = styled.div`
  overflow-y: scroll;
  max-height: 800px;
`;

/*
const ChatContainer = styled.div`
  flex: auto;
  background-color: rgb(58, 25, 119);
`;
*/
const ChatContainer = styled.div`
  flex: auto;
  background-color: #0f0f0f;
`;


export default GroupRoomChat