package com.visma.meet;

import com.google.gson.Gson;
import com.visma.meet.api.MeetingController;
import com.visma.meet.dao.Holder;
import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;
import com.visma.meet.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@WebMvcTest(value = MeetingController.class)
class MeetingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    @Test
    public void givenCorrectData_whenSchedulingMeeting_thenStatusOK() throws Exception {
        Holder.getPersonHolder().add(new Person(UUID.randomUUID(), "Jonas Jonaitis"));

        Meeting testMeeting = Meeting.createMeeting(
                UUID.randomUUID(),
                "Test meeting",
                Holder.getPersonHolder().get(0).getId(),
                "testing",
                "CodeMonkey",
                "InPerson",
                "2022-05-22 12:00:00",
                "2022-05-22 13:00:00");

        assert(testMeeting != null);

        Gson gson = new Gson();

        String json = gson.toJson(testMeeting);
        String finalJson = "{" + json.substring(json.indexOf(",") + 1, json.lastIndexOf(",")) + "}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("https://localhost:8080/api/v1/meeting/scheduleMeeting").content(finalJson).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }
}