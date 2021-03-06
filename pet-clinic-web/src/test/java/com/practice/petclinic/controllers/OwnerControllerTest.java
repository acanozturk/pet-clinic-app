package com.practice.petclinic.controllers;

import com.practice.petclinic.model.Owner;
import com.practice.petclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController ownerController;

    MockMvc mockMvc;
    Set<Owner> owners;

    @BeforeEach
    void setUp() {
        owners = new HashSet<>();
        Owner testOwner1 = new Owner();
        testOwner1.setId(1L);
        Owner testOwner2 = new Owner();
        testOwner2.setId(2L);
        owners.add(testOwner1);
        owners.add(testOwner2);

        mockMvc = MockMvcBuilders
                .standaloneSetup(ownerController)
                .build();
    }

    @Test
    void listOfOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void displayOwner() throws Exception {
        when(ownerService.findById(anyLong()))
                .thenReturn(owners.stream()
                        .filter(owner -> owner.getId().equals(1L))
                        .findFirst()
                        .get());

        mockMvc.perform(get("/owners/132"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", hasProperty("id", is(1L))));
    }

    @Test
    void findOwnerReturnOne() throws Exception {
        when(ownerService.findByLastNameContainingIgnoreCase(anyString()))
                .thenReturn(Arrays.asList(owners.stream()
                        .filter(owner -> owner.getId().equals(1L))
                        .findFirst()
                        .get()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
    }

    @Test
    void findOwnerReturnMany() throws Exception {
        List<Owner> ownerTest = new ArrayList<>();
        ownerTest.addAll(owners);
        when(ownerService.findByLastNameContainingIgnoreCase(anyString()))
                .thenReturn(ownerTest);

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));
    }

    @Test
    void initializeCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }

    @Test
    void processCreationForm() throws Exception {
        Owner testOwner = new Owner();
        testOwner.setId(3L);
        when(ownerService.save(ArgumentMatchers.any()))
                .thenReturn(testOwner);

        mockMvc.perform(post("/owners/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/3"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(ArgumentMatchers.any());
    }

    @Test
    void initializeUpdateOwnerForm() throws Exception {
        when(ownerService.findById(anyLong()))
                .thenReturn(owners.stream()
                        .filter(owner -> owner.getId().equals(1L))
                        .findFirst()
                        .get());

        mockMvc.perform(get("/owners/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).findById(anyLong());
    }

    @Test
    void processUpdateOwnerForm() throws Exception {
        Owner testOwner = new Owner();
        testOwner.setId(3L);
        when(ownerService.save(ArgumentMatchers.any()))
                .thenReturn(testOwner);

        mockMvc.perform(post("/owners/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/3"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).save(ArgumentMatchers.any());
    }

}