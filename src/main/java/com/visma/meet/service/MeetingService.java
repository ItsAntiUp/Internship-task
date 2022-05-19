package com.visma.meet.service;

import com.visma.meet.dao.MeetingDao;
import com.visma.meet.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {
    private final MeetingDao meetingDao;

    @Autowired
    public MeetingService(@Qualifier("MeetingDao") MeetingDao meetingDao) {
        this.meetingDao = meetingDao;
    }

    public ResponseEntity<String> scheduleMeeting(Meeting meeting){
        return meetingDao.scheduleMeeting(meeting);
    }

    public ResponseEntity<String> deleteMeeting(UUID meetingId, UUID executorId){
        return meetingDao.deleteMeeting(meetingId, executorId);
    }

    public ResponseEntity<String> addAttendee(UUID meetingId, UUID attendeeId){
        return meetingDao.addAttendee(meetingId, attendeeId);
    }

    public ResponseEntity<String> removeAttendee(UUID meetingId, UUID attendeeId){
        return meetingDao.removeAttendee(meetingId, attendeeId);
    }

    public ResponseEntity<List<Meeting>> listAndFilterMeetings(List<Object> filters){
        return meetingDao.listAndFilterMeetings(filters);
    }
}
