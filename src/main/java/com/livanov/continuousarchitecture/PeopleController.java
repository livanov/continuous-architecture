package com.livanov.continuousarchitecture;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/people")
@RequiredArgsConstructor
public class PeopleController {

    private final PeopleService people;

    @GetMapping
    public Stream<PersonDto> all() {
        return people.getAll().stream()
                .map(PersonDto::build);
    }

    @Data
    public static class PersonDto {

        private String id;
        private String name;
        private LocalDate dateOfBirth;

        public static PersonDto build(Person person) {
            val dto = new PersonDto();
            dto.setId(person.getId().toString());
            dto.setName(person.getName());
            dto.setDateOfBirth(person.getDateOfBirth());
            return dto;
        }
    }
}
