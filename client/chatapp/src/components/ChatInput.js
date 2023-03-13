import React, { useState } from 'react'
import { useSelector } from 'react-redux';
import styled from 'styled-components'

const ChatInput = ({sendMessage}) => {
  const [messageText,setMessageText] = useState("")
  const submitMessage = (e) => {
    e.preventDefault();
    const messageContent = messageText;
    setMessageText("");

    sendMessage(messageContent);

  }
  const changeMessage = (event) => {
    setMessageText(event.target.value)
  }

  return (
    <ChatInputContainer>
      <form onSubmit={submitMessage}>
        <InputBox name="message" id="message" value={messageText} onChange={changeMessage}/>
        <SendButton>Send</SendButton>
      </form>
      
    </ChatInputContainer>
  )
}

const ChatInputContainer = styled.div`
  display: flex;
  justify-content: center;
  margin: 5px;
`;

/*
const InputBox = styled.textarea`
  flex: 0.95;
  background-color: rgb(92, 25, 207);
  border: 1px solid saddlebrown;
  width: 200px;
  margin: 5px;
  height: 50px;
  resize: none;
  color: antiquewhite;
`;
*/

const InputBox = styled.textarea`
  background-color: #403e3e;
  border: 1px solid black;
  width: 500px;
  margin: 5px;
  height: 50px;
  resize: none;
  color: antiquewhite;
`;

/*
const SendButton = styled.button`
  border: 1px solid purple;
  background-color: rgb(38, 12, 83);
  color: antiquewhite;
  flex: 0.05;
  height: 50px;
  align-self: center;
  &:hover {
    background-color: rgb(57, 17, 126);
  }
`;
*/
const SendButton = styled.button`
  border: 1px solid black;
  background-color: #1c1a1a;
  color: antiquewhite;
  height: 40px;
  vertical-align: top;
  margin-top: 15px;
  &:hover {
    background-color: #302e2e;
  }
`;

export default ChatInput