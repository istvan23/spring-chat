import { render } from '@testing-library/react';
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux'
import { resetAppViewToInitialState } from './reducers/appViewReducer'
import { eraseAuthState } from './reducers/authReducer'
import { eraseGroupState } from './reducers/chatGroupReducer'
import { eraseMessageState } from './reducers/chatMessageReducer'
import { eraseAnnouncementState } from './reducers/groupAnnouncementReducer'
import { eraseRoomState } from './reducers/roomReducer'
import HomePage from './components/HomePage';
import MainPage from './components/MainPage';
function App() {
  const user = useSelector(state => state.user)
  const userDispatch = useDispatch();
  const dispatch = useDispatch();



  const logout = () => {
    console.log("logout");
    dispatch(resetAppViewToInitialState());
    dispatch(eraseAuthState());
    dispatch(eraseGroupState());
    dispatch(eraseRoomState());
    dispatch(eraseMessageState());
    dispatch(eraseAnnouncementState());
  }


  if(user.length == 0 || user.message != undefined){
    return (user.message != undefined ? <HomePage message={user.message} userDispatch={userDispatch}/> : <HomePage userDispatch={userDispatch}/>)
  }
  else{
    return (<MainPage logout={logout}/>)
  }

}

export default App;
