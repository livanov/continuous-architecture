package com.livanov.continuousarchitecture.domain;

import com.livanov.continuousarchitecture.persistence.DummyDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleRepository repository;

    @Secured("ROLE_ADMIN")
    public List<Person> getAll() {

        log.info("Fetching all people");

        return repository.findAll();
    }

    public void save(Person person) {
        repository.save(person);
    }
}
