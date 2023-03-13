import React, { useEffect } from 'react'
import { useSelector } from 'react-redux';
import styled from 'styled-components'
import { setActualRoomView, updatePage } from '../reducers/appViewReducer';
import { createRoom, getAllRoomOfGroup } from '../reducers/roomReducer';
import NewRoomForm from './NewRoomForm';

const GroupRoomListNav = ({dispatch}) => {
  const rooms = useSelector(state => state.room);
  const actualPage = useSelector(state => state.appView.openedGroupPages.length > 0 ? state.appView.openedGroupPages.find(groupState => groupState.groupId == state.appView.actualGroup) : null);
  const user = useSelector(state => state.user);
  

  const selectRoom = (event, room) => {
    if(actualPage != null){

      dispatch(setActualRoomView(room.id));
    }
  }

  useEffect(()=> {
    const interval = setInterval(() => {
      dispatch(getAllRoomOfGroup(user.access_token, actualPage.groupId))
    }, 1000*60);
    return () => clearInterval(interval);
    
  }, []);


  const newRoom = (roomName) => {
    
    const newRoom = {
      roomName: roomName,
      chatGroupId: actualPage.groupId
    }
    dispatch(createRoom(user.access_token, newRoom));
  }

  const deleteRoom = (event, room) => {
    // TODO
  }

  const renameRoom = (event, room) => {
    // TODO
  }

  const roomList = (
    actualPage != null ?
    (<RoomListContainer>
      {rooms.map(room => (<RoomSelectorButton key={room.id} onClick={(e) => selectRoom(e, room)}>{room.roomName}</RoomSelectorButton>))}
    </RoomListContainer>)
    : <p>No group selected</p>
    
  );
  

  /*
  const roomList = () => {
    if(actualPage != null){
      return(
        rooms.map(room => (<RoomSelectorButton onClick={(e) => selectRoom(e, room)}>{room.roomName}</RoomSelectorButton>))
      )
    }
    else{
      return(<p>No group selected</p>)
    }
  }
  */

  return (
    <div>
      {roomList}
      
      <br/>
      <NewRoomForm createNewRoom={newRoom}/>
    </div>
    
    
  )
}

/*
const RoomListContainer = styled.div`
  flex: 0.3;
  background-color: rgb(48, 20, 99);
  padding: 5px;
  max-width: 250px;
  height: 800px;
  overflow-y: scroll;
`;
*/

const RoomListContainer = styled.div`
  flex: 0.3;
  background-color: #1a1919;
  padding: 5px;
  max-width: 250px;
  height: 600px;
  overflow-y: scroll;
`;

/*
const RoomSelectorButton = styled.div`
  display: flex;
  flex-direction: column;
  color: antiquewhite;
  padding: 15px 5px 15px 5px;
  text-align: left;
  &:hover {
    background-color: rgb(69, 34, 134);
  }
`;
*/
const RoomSelectorButton = styled.button`
  display: flex;
  flex-direction: column;
  background-color: #1a1919;
  color: antiquewhite;
  padding: 15px 5px 15px 5px;
  border: 0px;
  width: 100%;
  text-align: left;
  &:hover {
    background-color: #302e2e;
  }
`;

export default GroupRoomListNav