package com.livanov.continuousarchitecture.web.dto;

import com.livanov.continuousarchitecture.domain.Person;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonDto {

    private String id;
    private String name;
    private LocalDate dateOfBirth;

    public static PersonDto from(Person person) {
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .dateOfBirth(person.getDateOfBirth())
                .build();
    }
}
