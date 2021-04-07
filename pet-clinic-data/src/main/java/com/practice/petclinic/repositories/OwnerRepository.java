package com.practice.petclinic.repositories;

import com.practice.petclinic.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Owner findByLastName(String lastName);

}
