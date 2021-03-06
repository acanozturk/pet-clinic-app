package com.practice.petclinic.services.jpa;

import com.practice.petclinic.model.Pet;
import com.practice.petclinic.repositories.PetRepository;
import com.practice.petclinic.services.PetService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class PetJpaService extends AbstractJpaService<Pet, PetRepository> implements PetService {

    public PetJpaService(PetRepository repository) {
        super(repository);
    }

    @Override
    public Pet findByNameContainingIgnoreCase(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
}
