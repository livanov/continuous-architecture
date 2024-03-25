package com.livanov.continuousarchitecture.persistence;

import com.livanov.continuousarchitecture.domain.PeopleService;
import com.livanov.continuousarchitecture.domain.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DummyDataGenerator {

    private final PeopleService peopleService;

    public void generate() {
        peopleService.save(new Person("Lyubo", LocalDate.of(1919, 2, 26)));
        peopleService.save(new Person("John", LocalDate.of(2020, 4, 22)));
        peopleService.save(new Person("Steve", LocalDate.of(2001, 12, 7)));
    }
}
