package com.visma.meet.dao;

import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("MeetingDao")
public class MeetingDataAccessService implements MeetingDao{
    @Override
    public ResponseEntity<String> scheduleMeeting(UUID id, Meeting meeting) {
        Meeting newMeeting = Meeting.createMeeting(id, meeting.getName(), meeting.getResponsiblePersonId(), meeting.getDescription(), meeting.getCategory(), meeting.getType(), meeting.getStartDateTime(), meeting.getEndDateTime());

        if(newMeeting == null)
            return new ResponseEntity<>(Utility.ERR_MEETING_FAILED_TO_CREATE, HttpStatus.BAD_REQUEST);

        Holder.getActiveMeetings().add(newMeeting);
        return new ResponseEntity<>(Utility.MSG_SCHEDULED_SUCCESS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteMeeting(UUID meetingId, UUID executorId){
        //Checking if the meeting and person are present
        Meeting meeting = Utility.findMeeting(Holder.getActiveMeetings(), meetingId);

        if(meeting == null)
            return new ResponseEntity<>(Utility.ERR_MEETING_NOT_FOUND, HttpStatus.BAD_REQUEST);

        Person person = Utility.findPerson(Holder.getPersonHolder(), executorId);

        if(person == null)
            return new ResponseEntity<>(Utility.ERR_PERSON_NOT_FOUND, HttpStatus.BAD_REQUEST);

        //Checking if the executor is the responsible person (if not - deleting is restricted)
        if(!person.getId().equals(meeting.getResponsiblePersonId()))
            return new ResponseEntity<>(Utility.ERR_DELETE_MEETING_RESTRICTED, HttpStatus.BAD_REQUEST);

        Holder.getActiveMeetings().removeIf(m -> m.getId().equals(meetingId));

        return new ResponseEntity<>(Utility.MSG_DELETE_SUCCESS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> addAttendee(UUID meetingId, UUID attendeeId){
        //Checking if the meeting and person are present
        Meeting meeting = Utility.findMeeting(Holder.getActiveMeetings(), meetingId);

        if(meeting == null)
            return new ResponseEntity<>(Utility.ERR_MEETING_NOT_FOUND, HttpStatus.BAD_REQUEST);

        Person person = Utility.findPerson(Holder.getPersonHolder(), attendeeId);

        if(person == null)
            return new ResponseEntity<>(Utility.ERR_PERSON_NOT_FOUND, HttpStatus.BAD_REQUEST);

        //Checking if this person is already in the meeting
        if(Utility.findPerson(meeting.getAttendees(), attendeeId) != null)
            return new ResponseEntity<>(Utility.ERR_PERSON_ALREADY_ADDED, HttpStatus.BAD_REQUEST);

        String responseMessage = "";

        //Checking if any other meeting would intersect with this one
        List<Meeting> otherMeetings =
                Holder.getActiveMeetings()
                        .stream()
                        .filter(m -> (Utility.findPerson(m.getAttendees(), attendeeId) != null) && !m.getId().equals(meetingId))
                        .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for(Meeting m : otherMeetings){
            LocalDateTime meetingStartDate = LocalDateTime.parse(meeting.getStartDateTime(), formatter);
            LocalDateTime meetingEndDate = LocalDateTime.parse(meeting.getEndDateTime(), formatter);
            LocalDateTime mStartDate = LocalDateTime.parse(m.getStartDateTime(), formatter);
            LocalDateTime mEndDate = LocalDateTime.parse(m.getEndDateTime(), formatter);

            if(meetingEndDate.isBefore(mStartDate) || meetingStartDate.isAfter(mEndDate)) {
                responseMessage = Utility.WRN_MEETING_COLLISION + m.getName();
                break;
            }
        }

        meeting.addAttendee(person);
        responseMessage += ("\n" + Utility.MSG_ATTENDEE_ADD_SUCCESS + "\n" + "Name: " + person.getName() + " Date added: " + LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> removeAttendee(UUID meetingId, UUID attendeeId){
        //Checking if the meeting and person are present
        Meeting meeting = Utility.findMeeting(Holder.getActiveMeetings(), meetingId);

        if(meeting == null)
            return new ResponseEntity<>(Utility.ERR_MEETING_NOT_FOUND, HttpStatus.BAD_REQUEST);

        Person person = Utility.findPerson(Holder.getPersonHolder(), attendeeId);

        if(person == null)
            return new ResponseEntity<>(Utility.ERR_PERSON_NOT_FOUND, HttpStatus.BAD_REQUEST);

        //Checking if the person is in the meeting
        if(!meeting.getAttendees().contains(person))
            return new ResponseEntity<>(Utility.ERR_PERSON_NOT_ADDED, HttpStatus.BAD_REQUEST);

        //Checking if the person is responsible for this meeting
        if(person.getId().equals(meeting.getResponsiblePersonId()))
            return new ResponseEntity<>(Utility.ERR_REMOVE_ATTENDEE_RESTRICTED, HttpStatus.BAD_REQUEST);

        meeting.removeAttendee(person);

        return new ResponseEntity<>(Utility.MSG_ATTENDEE_REMOVE_SUCCESS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Meeting>> listAndFilterMeetings(List<Object> filters) {
        Stream<Meeting> filteringStream = Holder.getActiveMeetings().stream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Filtering by each parameter, if it is not null
        String description = (String)filters.get(0);
        String responsiblePersonId = (String)filters.get(1);
        String category = (String)filters.get(2);
        String type = (String)filters.get(3);
        String startDateTime = (String)filters.get(4);
        String endDateTime = (String)filters.get(5);
        Integer attendeeNumber = (Integer)filters.get(6);

        if(description != null) {
            Predicate<Meeting> descriptionPredicate = obj -> obj.getDescription().toLowerCase().contains(description.toLowerCase());
            filteringStream = filteringStream.filter(descriptionPredicate);
        }
        if(responsiblePersonId != null) {
            Predicate<Meeting> responsiblePersonPredicate = obj -> obj.getResponsiblePersonId().equals(UUID.fromString(responsiblePersonId));
            filteringStream = filteringStream.filter(responsiblePersonPredicate);
        }
        if(category != null) {
            Predicate<Meeting> categoryPredicate = obj -> obj.getCategory().equals(category);
            filteringStream = filteringStream.filter(categoryPredicate);
        }
        if(type != null) {
            Predicate<Meeting> typePredicate = obj -> obj.getType().equals(type);
            filteringStream = filteringStream.filter(typePredicate);
        }
        if(startDateTime != null) {
            LocalDateTime startDate = LocalDateTime.parse(startDateTime, formatter);
            Predicate<Meeting> startDateTimePredicate = obj -> LocalDateTime.parse(obj.getStartDateTime(), formatter).isAfter(startDate) || obj.getStartDateTime().equals(startDateTime);
            filteringStream = filteringStream.filter(startDateTimePredicate);
        }
        if(endDateTime != null) {
            LocalDateTime endDate = LocalDateTime.parse(endDateTime, formatter);
            Predicate<Meeting> endDateTimePredicate = obj -> LocalDateTime.parse(obj.getEndDateTime(), formatter).isBefore(endDate) || obj.getEndDateTime().equals(endDateTime);
            filteringStream = filteringStream.filter(endDateTimePredicate);
        }
        if(attendeeNumber != null) {
            Predicate<Meeting> attendeeNumberPredicate = obj -> obj.getAttendees().size() > attendeeNumber;
            filteringStream = filteringStream.filter(attendeeNumberPredicate);
        }

        return new ResponseEntity<>(filteringStream.collect(Collectors.toList()), HttpStatus.OK);
    }
}
