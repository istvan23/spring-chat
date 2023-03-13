package com.example.springchatserver.service.group;

import com.example.springchatserver.dto.GroupAnnouncementDto;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupAnnouncementService {
    List<GroupAnnouncementDto> getAllAnnouncementsOfGroupById(Long groupId);
    GroupAnnouncementDto getAnnouncementById(Long announcementId);
    GroupAnnouncementDto addNewAnnouncement(GroupAnnouncementDto groupAnnouncement);
    GroupAnnouncementDto updateAnnouncement(GroupAnnouncementDto groupAnnouncement);
    List<GroupAnnouncementDto> getAnnouncementsOfGroupByTimeInterval(Long groupId, LocalDateTime from, LocalDateTime to);
    void deleteAnnouncement(Long announcementId);
}
