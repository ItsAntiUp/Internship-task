package com.visma.meet.dao;

import com.visma.meet.model.Meeting;
import com.visma.meet.model.Person;

import java.util.ArrayList;
import java.util.List;

public class Holder {
    private static List<Person> personHolder = new ArrayList<>();
    private static List<Meeting> activeMeetings = new ArrayList<>();

    public static List<Person> getPersonHolder() {
        return personHolder;
    }

    public static List<Meeting> getActiveMeetings() {
        return activeMeetings;
    }

    public static void setPersonHolder(List<Person> personHolder) {
        Holder.personHolder = personHolder;
    }

    public static void setActiveMeetings(List<Meeting> activeMeetings) {
        Holder.activeMeetings = activeMeetings;
    }
}
