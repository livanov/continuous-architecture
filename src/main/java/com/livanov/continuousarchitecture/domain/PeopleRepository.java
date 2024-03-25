package com.livanov.continuousarchitecture.domain;

import org.springframework.data.repository.ListCrudRepository;

interface PeopleRepository extends ListCrudRepository<Person, String> {
}
