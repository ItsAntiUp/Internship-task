package com.visma.meet.service;

import com.visma.meet.dao.PersonDao;
import com.visma.meet.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonDao personDao;

    @Autowired
    public PersonService(@Qualifier("PersonDao") PersonDao personDao) {
        this.personDao = personDao;
    }

    public ResponseEntity<String> addPerson(Person person){
        return personDao.addPerson(person);
    }
}
