import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux'
import { updateRoleOfMember } from '../../reducers/membershipReducer';
import styled from 'styled-components';
import { addGroupRole } from '../../reducers/groupRoleReducer';

const MemberInfo = ({member, dispatch, acceptJoinRequest}) => {

    /*
    const [username, setUsername] = useState(member.username);
    const [userId, setUserId] = useState(member.id);
    */
    const user = useSelector(state => state.user);
    const givenMember = useSelector(state => state.membership.find(x => x.id == member.id))

    /*
    const [roleName, setRoleName] = useState(member.role != null ? member.role.name : "");
    const [roleDisplayName, setRoleDisplayName] = useState(member.role != null ? member.role.displayName : "");
    const [authorities, setAuthorities] = useState(member.role != null ? member.role.authorities : "");
    const [isModify, setIsModify] = useState(false);
    const availableAuthorityOptions = ["GROUP_READ", "GROUP_UPDATE"]
    const [selectedNewAuthorityOption, setSelectedNewAuthorityOption] = useState("");
    */

    /*
    useEffect(()=> {
        if(member != null)
        {
            setRoleName(member.role.name);
            setRoleDisplayName(member.role.displayName);
            setAuthorities(member.role.authorities);
        }

    }, [member])
    */

    const grantFullMembership = (e) => {
        acceptJoinRequest(user.access_token,member);
    }


    /*
    const selectMembershipRoleToModify = (e) => {

    }

    const changeRoleName = (e) => {
        setRoleName(e.target.value);
    }

    const changeRoleDisplayName = (e) => {
        setRoleDisplayName(e.target.value);
    }

    const removeAuthority = (e, authority) => {
        let modifiedAuthorities = authorities;
        modifiedAuthorities = modifiedAuthorities.splice(modifiedAuthorities.findIndex(element => element == authority), 1);
        setAuthorities(modifiedAuthorities);
    }
    const addAuthority = () => {
        let modifiedAuthorities = authorities;
        const privilegeObject = {
            name: selectedNewAuthorityOption
        };
        modifiedAuthorities.push(privilegeObject);
        setAuthorities(modifiedAuthorities);
    }
    const save = () => {
        const role = {
            ...member.role,
            name: roleName,
            displayName: roleDisplayName,
            authorities: authorities
        };
        dispatch(updateRoleOfMember(user.access_token, role, member.chatGroup.id));
    }
    */

    return (
        <MemberInfoContainer>
            <IndividualDescriptionContainer>
                <div>
                    ID: {givenMember.id}<br/> Username: {givenMember.username}
                </div> <br/>
                <div>
                    Role: {givenMember.role != null ? givenMember.role.displayName : "Member candidate"} <br/>
                    Authorities: {givenMember.role != null && givenMember.authorities != undefined ? (givenMember.authorities.map(authority => authority.name + ", ")) : "NONE"}
                </div>
                {givenMember.role != null ? <div><MembershipActionButton>Modify Role</MembershipActionButton>
                <MembershipActionButton>Kick</MembershipActionButton> </div> : <MembershipActionButton onClick={grantFullMembership}>Accept</MembershipActionButton>}
            </IndividualDescriptionContainer>
        </MemberInfoContainer>
      )

    /*
  return (
    <MemberInfoContainer>
        <IndividualDescriptionContainer>
            <div>
                ID: {member.id}<br/> Username: {member.username}
            </div> <br/>
            <div>
                Role: {roleDisplayName} <br/>
                Authorities: {authorities.map(authority => authority.name + ", ")}
            </div>
        </IndividualDescriptionContainer>
        
        <br/>
        {isModify ? 
        <div>
            Role name: <input type="text" value={roleName} onChange={changeRoleName}/> <br/>
            Role display name: <input type="text" value={roleDisplayName} onChange={changeRoleDisplayName}/><br/>
            Authorities: {authorities.map(authority => (<p>{authority.name} <button onClick={(e) => removeAuthority(e, authority)}>delete</button></p>))}<br/>
            Add new authority: 
            <select value={selectedNewAuthorityOption} onChange={setSelectedNewAuthorityOption}>
                {availableAuthorityOptions.map(authority => <option value={authority}>{authority}</option>)}
            </select>
            <button onClick={() => addAuthority()}>Add</button><br/>
            <button onClick={() => save()}>Save</button>
            
        </div> : null}
    </MemberInfoContainer>
  )
  */
}


const MemberInfoContainer = styled.div`
`;
const IndividualDescriptionContainer = styled.div`
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

const MembershipActionButton = styled.button`
  background-color: #1f1a1a;
  color: antiquewhite;
  height: 30px;
  min-width: 40px;
  margin-right: 5px;
  margin-top: 5px;
  border: 1px solid black;
  &:hover {
    background-color: #2e2828;
  }
`;

export default MemberInfo