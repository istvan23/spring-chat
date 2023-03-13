package com.example.springchatserver.service.group;

import com.example.springchatserver.domain.ChatGroup;
import com.example.springchatserver.domain.GroupAnnouncement;
import com.example.springchatserver.domain.User;
import com.example.springchatserver.dto.GroupAnnouncementDto;
import com.example.springchatserver.mapper.GroupAnnouncementMapper;
import com.example.springchatserver.repository.GroupAnnouncementRepository;
import com.example.springchatserver.repository.GroupRepository;
import com.example.springchatserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class GroupAnnouncementServiceImpl implements GroupAnnouncementService {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupAnnouncementRepository groupAnnouncementRepository;
    private GroupAnnouncementMapper groupAnnouncementMapper;

    @Override
    public List<GroupAnnouncementDto> getAllAnnouncementsOfGroupById(Long groupId) {
        ChatGroup chatGroup = this.groupRepository.findById(groupId).orElseThrow(NoSuchElementException::new);
        List<GroupAnnouncement> groupAnnouncements = chatGroup.getGroupAnnouncements();
        List<GroupAnnouncementDto> groupAnnouncementDtos = StreamSupport
                .stream(groupAnnouncements.stream().spliterator(), false)
                .map(x -> this.groupAnnouncementMapper.convertToDto(x))
                .collect(Collectors.toList());
        return groupAnnouncementDtos;
    }

    @Override
    public GroupAnnouncementDto getAnnouncementById(Long announcementId) {
        GroupAnnouncementDto groupAnnouncementDto = null;
        GroupAnnouncement groupAnnouncement = this.groupAnnouncementRepository.findById(announcementId).get();
        if(groupAnnouncement != null){
            groupAnnouncementDto = this.groupAnnouncementMapper.convertToDto(groupAnnouncement);
        }
        return groupAnnouncementDto;
    }

    @Override
    public List<GroupAnnouncementDto> getAnnouncementsOfGroupByTimeInterval(Long groupId, LocalDateTime from, LocalDateTime to) {
        ChatGroup chatGroup = this.groupRepository.findById(groupId).get();
        List<GroupAnnouncementDto> groupAnnouncementDtoList = null;
        if(!chatGroup.getGroupAnnouncements().isEmpty()){
            groupAnnouncementDtoList = chatGroup
                    .getGroupAnnouncements()
                    .stream()
                    .filter(groupAnnouncement -> groupAnnouncement.getDateOfLastModification().isAfter(from) && groupAnnouncement.getDateOfLastModification().isBefore(to))
                    .map(groupAnnouncement -> groupAnnouncementMapper.convertToDto(groupAnnouncement))
                    .collect(Collectors.toList());
        }
        return groupAnnouncementDtoList;
    }

    @Override
    public GroupAnnouncementDto addNewAnnouncement(GroupAnnouncementDto announcement) {
        GroupAnnouncement groupAnnouncement = new GroupAnnouncement();
        User author = this.userRepository.findByUsername(announcement.getAuthorUsername()).get();
        ChatGroup group = this.groupRepository.findById(announcement.getChatGroupId()).get();
        groupAnnouncement.setAuthor(author);
        groupAnnouncement.setChatGroup(group);
        groupAnnouncement.setTitle(announcement.getTitle());
        groupAnnouncement.setText(announcement.getText());
        LocalDateTime dateTime = LocalDateTime.now();
        groupAnnouncement.setDateOfCreation(dateTime);
        groupAnnouncement.setDateOfLastModification(dateTime);

        GroupAnnouncementDto saved = null;
        try{
            saved = this.groupAnnouncementMapper.convertToDto(this.groupAnnouncementRepository.save(groupAnnouncement));
        }catch (DataIntegrityViolationException e){
            throw e;
        }

        return saved;
    }

    @Override
    public GroupAnnouncementDto updateAnnouncement(GroupAnnouncementDto announcement) {
        GroupAnnouncement groupAnnouncement = null;
        try{
            groupAnnouncement = this.groupAnnouncementRepository.findById(announcement.getId()).get();
        }catch (DataAccessException e){
            throw e;
        }
        groupAnnouncement.setTitle(announcement.getTitle());
        groupAnnouncement.setText(announcement.getText());
        groupAnnouncement.setDateOfLastModification(LocalDateTime.now());
        groupAnnouncement = this.groupAnnouncementRepository.save(groupAnnouncement);

        return this.groupAnnouncementMapper.convertToDto(groupAnnouncement);
    }

    @Override
    public void deleteAnnouncement(Long announcementId) {
        this.groupAnnouncementRepository.deleteById(announcementId);
    }
}
