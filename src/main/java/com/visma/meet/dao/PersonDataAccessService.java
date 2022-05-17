package com.visma.meet.dao;

import com.visma.meet.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("PersonDao")
public class PersonDataAccessService implements PersonDao{
    @Override
    public ResponseEntity<String> addPerson(UUID id, Person person) {
        Holder.getPersonHolder().add(new Person(id, person.getName()));
        return new ResponseEntity<>(Utility.MSG_PERSON_ADDED_SUCCESS, HttpStatus.OK);
    }
}
