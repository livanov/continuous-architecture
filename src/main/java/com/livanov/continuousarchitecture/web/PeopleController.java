package com.livanov.continuousarchitecture.web;

import com.livanov.continuousarchitecture.domain.PeopleService;
import com.livanov.continuousarchitecture.web.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("api/people")
@RequiredArgsConstructor
class PeopleController {

    private final PeopleService people;

    @GetMapping
    public Stream<PersonDto> all() {
        return people.getAll().stream()
                .map(PersonDto::from);
    }
}
