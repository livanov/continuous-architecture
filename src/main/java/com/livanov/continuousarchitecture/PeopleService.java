package com.livanov.continuousarchitecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PeopleService {

    private static final Logger log = Logger.getLogger(PeopleService.class.getName());

    @Autowired
    private PeopleRepository repository;

    public List<Person> getAll() {

        log.info("Fetching all people");

        return repository.findAll();
    }
}
