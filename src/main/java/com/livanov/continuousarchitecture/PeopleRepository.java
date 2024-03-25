package com.livanov.continuousarchitecture;

import org.springframework.data.repository.ListCrudRepository;

public interface PeopleRepository extends ListCrudRepository<Person, String> {
}
