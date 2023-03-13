import React from 'react'

const AnnouncementBlock = ({announcement}) => {
  return (
    <div>
      <div>Published in  {announcement.dateOfCreation} by {announcement.authorUsername} {announcement.dateOfCreation != announcement.dateOfLastModification ? <i>last modified {announcement.dateOfLastModification}</i> : null}</div>
      <div><p><h3>{announcement.title}</h3></p></div><br/><br/>
      <div>
        <p>
          {announcement.text}
        </p>
      </div>
      <hr/>
    </div>
  )
}

export default AnnouncementBlock