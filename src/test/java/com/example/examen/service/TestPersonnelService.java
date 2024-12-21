package com.example.examen.service;


import com.example.examen.model.CustomUser;
import com.example.examen.model.Personnel;
import com.example.examen.repository.PersonnelRepository;
import com.example.examen.service.IUserService;
import com.example.examen.service.PersonnelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestPersonnelService {

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private PersonnelRepository personnelRepository;

    @Mock
    private IUserService userService;

    @InjectMocks
    private PersonnelService personnelService;


    @Test
    void testFindAllPersonnel () {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Personnel> personnelPage = new PageImpl<>(List.of(new Personnel()));

        when(personnelRepository.findAll(pageable)).thenReturn(personnelPage);

        Page<Personnel> result = personnelService.findAllPersonnel(pageable);

        assertEquals(1, result.getContent().size());
        verify(personnelRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindPersonnelById () {
        Long id = 1L;
        Personnel personnel = new Personnel();
        personnel.setId(id);

        when(personnelRepository.findById(id)).thenReturn(Optional.of(personnel));

        Optional<Personnel> result = personnelService.findPersonnelById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(personnelRepository, times(1)).findById(id);
    }

    @Test
    void testDeletePersonnelById_Success () {
        Long id = 1L;
        Personnel personnel = new Personnel();
        CustomUser user = new CustomUser();
        user.setPersonnelList(new ArrayList<>(List.of(personnel)));
        personnel.setCustomUser(user);

        when(personnelRepository.findById(id)).thenReturn(Optional.of(personnel));

        String result = personnelService.deletePersonnelById(id);

        assertEquals("Personnel med id: 1 borttagen", result);
        verify(personnelRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletePersonnelById_NotFound () {
        Long id = 1L;
        when(personnelRepository.findById(id)).thenReturn(Optional.empty());

        String result = personnelService.deletePersonnelById(id);

        assertEquals("Personnel med id: 1 kunde inte hittas", result);
        verify(personnelRepository, never()).deleteById(id);
    }

    @Test
    void testSavePersonnel_NewPersonnel () {
        Personnel personnel = new Personnel();
        personnel.setPicture("[]".getBytes());
        CustomUser user = new CustomUser();
        user.setUsername("testUser");

        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn("testUser");

        when(userService.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(personnelRepository.save(personnel)).thenReturn(personnel);

        personnelService.savePersonnel(personnel);

        assertEquals(user, personnel.getCustomUser());
        verify(personnelRepository, times(1)).save(personnel);
    }
}
