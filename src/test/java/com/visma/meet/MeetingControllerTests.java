package com.visma.meet;

import com.google.gson.Gson;
import com.visma.meet.api.MeetingController;
import com.visma.meet.dao.Holder;
import com.visma.meet.dao.Utility;
import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;
import com.visma.meet.service.MeetingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MeetingController.class)
class MeetingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    private static Meeting testMeeting;
    private static Gson gson;

    @BeforeAll
    public static void setUp(){
        Holder.getPersonHolder().add(new Person(UUID.randomUUID(), "Jonas Jonaitis"));
        Holder.getPersonHolder().add(new Person(UUID.randomUUID(), "Petras Petraitis"));

        testMeeting = Meeting.createMeeting(
                UUID.randomUUID(),
                "Test meeting",
                Holder.getPersonHolder().get(0).getId(),
                "testing",
                "CodeMonkey",
                "InPerson",
                "2022-05-22 12:00:00",
                "2022-05-22 13:00:00");

        assert(testMeeting != null);

        Holder.getActiveMeetings().add(testMeeting);
        gson = new Gson();
    }

    @Test
    public void givenCorrectData_whenSchedulingMeeting_thenStatusOK() throws Exception {
        String json = gson.toJson(testMeeting);
        String finalJson = "{" + json.substring(json.indexOf(",") + 1, json.lastIndexOf(",")) + "}";

        Mockito.when(meetingService.scheduleMeeting(Mockito.any()))
                .thenReturn(new ResponseEntity<>(Utility.MSG_SCHEDULED_SUCCESS, HttpStatus.OK));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("https://localhost:8080/api/v1/meeting/scheduleMeeting")
                .content(finalJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void givenCorrectData_whenListingMeetings_thenStatusOK() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("description", "meeting");

        List<Meeting> mockResponse = new ArrayList<>();
        mockResponse.add(testMeeting);

        Mockito.when(meetingService.listAndFilterMeetings(Mockito.any()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("https://localhost:8080/api/v1/meeting/getMeetings")
                .queryParams(params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void givenNotResponsiblePerson_whenDeletingMeeting_thenStatusBadRequest() throws Exception {
        String query = "//localhost:8080/api/v1/meeting/deleteMeeting?meetingId=" +
                testMeeting.getId().toString() +
                "&executorId=" +
                Holder.getPersonHolder().get(1).getId().toString();

        Mockito.when(meetingService.deleteMeeting(testMeeting.getId(), Holder.getPersonHolder().get(1).getId()))
                .thenReturn(new ResponseEntity<>(Utility.ERR_DELETE_MEETING_RESTRICTED, HttpStatus.BAD_REQUEST));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(query)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }
}