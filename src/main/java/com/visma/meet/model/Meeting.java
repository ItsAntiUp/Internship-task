package com.visma.meet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.visma.meet.dao.Holder;
import com.visma.meet.dao.Utility;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Meeting {
    //Enums for creating fixed values

    private enum Category{
        CODEMONKEY("CodeMonkey"),
        HUB("Hub"),
        SHORT("Short"),
        TEAMBUILDING("TeamBuilding");

        private String value;

        Category(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }

    private enum Type{
        LIVE("Live"),
        INPERSON("InPerson");

        private String value;

        Type(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }

    private UUID id;
    private String name;
    private UUID responsiblePersonId;
    private String description;
    private String category;
    private String type;
    private String startDateTime;
    private String endDateTime;

    private List<Person> attendees;

    public static Meeting createMeeting(UUID id, String name, UUID responsiblePersonId, String description, String category, String type, String startDateTime, String endDateTime) {
        //Checking if the category is valid
        if(Arrays.stream(Category.values()).noneMatch(obj -> obj.getValue().equals(category)))
            return null;

        //Checking if the type is valid
        if(Arrays.stream(Type.values()).noneMatch(obj -> obj.getValue().equals(type)))
            return null;

        //Checking if the responsible person is valid
        Person person = Utility.findPerson(Holder.getPersonHolder(), responsiblePersonId);

        if(person == null)
            return null;

        //Checking if the dates are valid
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateTime, formatter);

        if(startDate.isAfter(endDate) || startDate.equals(endDate))
            return null;

        return new Meeting(id, name, responsiblePersonId, description, category, type, startDateTime, endDateTime);
    }

    private Meeting(@JsonProperty("id") UUID id,
                    @JsonProperty("name") String name,
                    @JsonProperty("responsiblePerson") UUID responsiblePersonId,
                    @JsonProperty("description") String description,
                    @JsonProperty("category") String category,
                    @JsonProperty("type") String type,
                    @JsonProperty("startDate") String startDateTime,
                    @JsonProperty("endDate") String endDateTime) {
        this.id = id;
        this.name = name;
        this.responsiblePersonId = responsiblePersonId;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        attendees = new ArrayList<Person>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getResponsiblePersonId() {
        return responsiblePersonId;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public List<Person> getAttendees() {
        return attendees;
    }

    public void addAttendee(Person attendee){
        attendees.add(attendee);
    }

    public void removeAttendee(Person attendee){
        attendees.removeIf(obj -> obj.getId().equals(attendee.getId()));
    }
}
