package com.visma.meet.dao;

import com.visma.meet.model.Meeting;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public interface MeetingDao {
    ResponseEntity<String> scheduleMeeting(UUID id, Meeting meeting);

    default ResponseEntity<String> scheduleMeeting(Meeting meeting){
        UUID id = UUID.randomUUID();
        return scheduleMeeting(id, meeting);
    }

    ResponseEntity<String> deleteMeeting(UUID meetingId, UUID executorId);

    ResponseEntity<String> addAttendee(UUID meetingId, UUID attendeeId);

    ResponseEntity<String> removeAttendee(UUID meetingId, UUID attendeeId);

    ResponseEntity<List<Meeting>> listAndFilterMeetings(List<Object> filters);
}
