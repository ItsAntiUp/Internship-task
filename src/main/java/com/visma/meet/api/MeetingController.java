package com.visma.meet.api;

import com.visma.meet.model.Meeting;
import com.visma.meet.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/meeting")
@RestController
public class MeetingController {
    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService service) {
        this.meetingService = service;
    }

    //localhost:8080/api/v1/meeting/scheduleMeeting{body}
    @RequestMapping(value = "/scheduleMeeting", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> scheduleMeeting(@RequestBody @NonNull Meeting meeting){
        return meetingService.scheduleMeeting(meeting);
    }

    //localhost:8080/api/v1/meeting/deleteMeeting?meetingId=  &executorId=
    @RequestMapping(value = "/deleteMeeting", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteMeeting(@RequestParam(value = "meetingId", required = true) String meetingId, @RequestParam(value = "executorId", required = true) String executorId){
        return meetingService.deleteMeeting(UUID.fromString(meetingId), UUID.fromString(executorId));
    }

    //localhost:8080/api/v1/meeting/addAttendee?meetingId=  &attendeeId=
    @RequestMapping(value = "/addAttendee", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> addAttendee(@RequestParam(value = "meetingId", required = true) String meetingId, @RequestParam(value = "attendeeId", required = true) String attendeeId){
        return meetingService.addAttendee(UUID.fromString(meetingId), UUID.fromString(attendeeId));
    }

    //localhost:8080/api/v1/meeting/removeAttendee?meetingId=  &attendeeId=
    @RequestMapping(value = "/removeAttendee", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> removeAttendee(@RequestParam(value = "meetingId", required = true) String meetingId, @RequestParam(value = "attendeeId", required = true) String attendeeId){
        return meetingService.removeAttendee(UUID.fromString(meetingId), UUID.fromString(attendeeId));
    }

    //localhost:8080/api/v1/meeting/getMeetings?description=  &responsiblePersonId= &category= &type= &startDateTime= &endDateTime= &attendeeNumber=
    @RequestMapping(value = "/getMeetings", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Meeting>> listAndFilterMeetings(@RequestParam(value = "description", required = false) String description,
                                                               @RequestParam(value = "responsiblePersonId", required = false) String responsiblePersonId,
                                                               @RequestParam(value = "category", required = false) String category,
                                                               @RequestParam(value = "type", required = false) String type,
                                                               @RequestParam(value = "startDateTime", required = false) String startDateTime,
                                                               @RequestParam(value = "endDateTime", required = false) String endDateTime,
                                                               @RequestParam(value = "attendeeNumber", required = false) Integer attendeeNumber){
        List<Object> filters = new ArrayList<>();

        filters.add(description);
        filters.add(responsiblePersonId);
        filters.add(category);
        filters.add(type);
        filters.add(startDateTime);
        filters.add(endDateTime);
        filters.add(attendeeNumber);

        return meetingService.listAndFilterMeetings(filters);
    }
}
