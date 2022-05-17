package com.visma.meet.dao;

import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;

import java.util.List;
import java.util.UUID;

public class Utility {
    public static final String MSG_SCHEDULED_SUCCESS = "Meeting scheduled successfully!";
    public static final String MSG_DELETE_SUCCESS = "Meeting deleted successfully!";
    public static final String MSG_ATTENDEE_ADD_SUCCESS = "Attendee was added successfully!";
    public static final String MSG_ATTENDEE_REMOVE_SUCCESS = "Attendee was removed successfully!";
    public static final String MSG_PERSON_ADDED_SUCCESS = "Person was added successfully.";
    public static final String MSG_NO_PEOPLE_FOUND = "No people found.";
    public static final String MSG_NO_MEETINGS_FOUND = "No meetings found.";

    public static final String WRN_MEETING_COLLISION = "WARNING - This meeting collides with the meeting: ";

    public static final String ERR_DELETE_MEETING_RESTRICTED = "ERROR - only the responsible person can delete this meeting.";
    public static final String ERR_PERSON_ALREADY_ADDED = "ERROR - This person is already added to the meeting.";
    public static final String ERR_PERSON_NOT_ADDED = "ERROR - This person is not added to the selected meeting.";
    public static final String ERR_PERSON_NOT_FOUND = "ERROR - The person was not found.";
    public static final String ERR_MEETING_NOT_FOUND = "ERROR - The meeting was not found.";
    public static final String ERR_REMOVE_ATTENDEE_RESTRICTED = "ERROR - The responsible person can not be removed from the meeting.";
    public static final String ERR_MEETING_FAILED_TO_CREATE = "ERROR - The meeting could not be created.";
    public static final String ERR_IO_READING = "ERROR - IO error while reading from a file.";
    public static final String ERR_IO_WRITING = "ERROR - IO error while writing to a file.";

    public static Meeting findMeeting(List<Meeting> meetings, UUID meetingId){
        if(meetings == null)
            return null;

        return meetings
                .stream()
                .filter(m -> meetingId.equals(m.getId()))
                .findAny()
                .orElse(null);
    }

    public static Person findPerson(List<Person> personHolder, UUID personId){
        if(personHolder == null)
            return null;

        return personHolder
                .stream()
                .filter(p -> personId.equals(p.getId()))
                .findAny()
                .orElse(null);
    }
}
