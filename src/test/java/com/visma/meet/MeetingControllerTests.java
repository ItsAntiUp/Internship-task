package com.visma.meet;

import com.google.gson.Gson;
import com.visma.meet.api.MeetingController;
import com.visma.meet.dao.Holder;
import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;
import com.visma.meet.service.MeetingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;;

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
        gson = new Gson();
    }

    @Test
    public void givenCorrectData_whenSchedulingMeeting_thenStatusOK() throws Exception {
        String json = gson.toJson(testMeeting);
        String finalJson = "{" + json.substring(json.indexOf(",") + 1, json.lastIndexOf(",")) + "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("https://localhost:8080/api/v1/meeting/scheduleMeeting")
                .content(finalJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void givenCorrectData_whenListingWithNoParameters_thenStatusOK() throws Exception {
        String json = gson.toJson(testMeeting);
        String finalJson = "{" + json.substring(json.indexOf(",") + 1, json.lastIndexOf(",")) + "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("https://localhost:8080/api/v1/meeting/getMeetings?description=&responsiblePersonId=&category=&type=&startDateTime=&endDateTime=&attendeeNumber=-1")
                .content(finalJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
}