package com.visma.meet;

import com.visma.meet.dao.Holder;
import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class MeetingCreationTests {

    @BeforeAll
    public static void setUp(){
        Holder.getPersonHolder().add(new Person(UUID.randomUUID(), "Jonas Jonaitis"));
    }

    @Test
    public void givenCorrectData_whenCreatingMeeting_thenMeetingNotNull(){
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
    }

    @Test
    public void givenWrongCategory_whenCreatingMeeting_thenMeetingNull(){
        Meeting testMeeting = Meeting.createMeeting(
                UUID.randomUUID(),
                "Test meeting",
                Holder.getPersonHolder().get(0).getId(),
                "testing",
                "---",
                "InPerson",
                "2022-05-22 12:00:00",
                "2022-05-22 13:00:00");

        assert(testMeeting == null);
    }

    @Test
    public void givenWrongType_whenCreatingMeeting_thenMeetingNull(){
        Meeting testMeeting = Meeting.createMeeting(
                UUID.randomUUID(),
                "Test meeting",
                Holder.getPersonHolder().get(0).getId(),
                "testing",
                "CodeMonkey",
                "---",
                "2022-05-22 12:00:00",
                "2022-05-22 13:00:00");

        assert(testMeeting == null);
    }

    @Test
    public void givenWrongDates_whenCreatingMeeting_thenMeetingNull(){
        Meeting testMeeting = Meeting.createMeeting(
                UUID.randomUUID(),
                "Test meeting",
                Holder.getPersonHolder().get(0).getId(),
                "testing",
                "CodeMonkey",
                "InPerson",
                "2022-05-22 15:00:00",
                "2022-05-22 13:00:00");

        assert(testMeeting == null);
    }
}