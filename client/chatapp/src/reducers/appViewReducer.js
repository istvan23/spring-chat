import {createSlice} from '@reduxjs/toolkit'

const appViewSlice = createSlice({
    name: 'appView',
    initialState: {
        actualGroup: -1,
        actualRoom: -1,
        groupSubPageView: 0, // its indicates that, the application which one should to present the group chat = 0 or group management/information = 1 or group announcements = 2
        showGroupSelectorPage: true,
        openedGroupPages: []
    },
    reducers: {
        setActualGroupView(state, action){

           const groupId = action.payload;
           const page = state.openedGroupPages.find(x => x.groupId == groupId);
           return {
            ...state,
            actualGroup: page.groupId,
            actualRoom: page.actualRoom
           }
        },
        setActualRoomView(state, action){
            return {
                ...state,
                actualRoom: action.payload
            }
        },
        setGroupSubPageView(state, action){
            return {
                ...state,
                groupSubPageView: action.payload
            }
        },
        openNewPage(state, action){

            if(state.openedGroupPages.some(groupPage => groupPage.groupId == action.payload.groupId)){
                return state;
            }
            return {
                ...state,
                openedGroupPages: [...state.openedGroupPages, action.payload],
            }
            
        },
        closePage(state, action){
            const alreadyOpenedPages = state.openedGroupPages;
            const id = alreadyOpenedPages.findIndex(page => page.id == action.payload);
            alreadyOpenedPages.splice(id, 1);
            return {
                openedGroupPages: alreadyOpenedPages,
                ...state
            }
        },
        updatePage(state, action){
            const alreadyOpenedPages = state.openedGroupPages;
            const index = alreadyOpenedPages.findIndex(page => page.id == action.payload.groupId);
            alreadyOpenedPages[index] = action.payload;
            return {
                ...state,
                openedGroupPages: alreadyOpenedPages,
            }
        },
        updatePageWithActualAppViewState(state){
            const index = state.openedGroupPages.findIndex(page => page.groupId == state.actualGroup);
            const alreadyOpenedPages = state.openedGroupPages;
            const updatablePage = {
                ...alreadyOpenedPages[index],
                groupId: state.actualGroup,
                actualRoom: state.actualRoom
            }
            /*
            alreadyOpenedPages[index] = {
                ...alreadyOpenedPages[index],
                groupId: state.actualGroup,
                actualRoom: state.actualRoom
            }*/
            const updatedOpenedPages = alreadyOpenedPages.map(page => page.groupId == updatablePage.groupId ? updatablePage : page);
            return {
                ...state,
                openedGroupPages: updatedOpenedPages
            }
        },
        toggleSelectorPage(state, action){
            
            return {
                ...state,
                showGroupSelectorPage: action.payload,//!state.showGroupSelectorPage,
            }
            
           //state.showGroupSelectorPage = action.payload;
        },
        resetAppViewToInitialState(state) {
            return {
                actualGroup: -1,
                actualRoom: -1,
                groupSubPageView: 0,
                showGroupSelectorPage: true,
                openedGroupPages: []
            }
        }
    }
})

export const {
    setActualGroupView, 
    setActualRoomView, 
    setGroupSubPageView, 
    openNewPage, 
    closePage, 
    updatePage, 
    toggleSelectorPage, 
    updatePageWithActualAppViewState, 
    resetAppViewToInitialState
} = appViewSlice.actions
export default appViewSlice.reducer