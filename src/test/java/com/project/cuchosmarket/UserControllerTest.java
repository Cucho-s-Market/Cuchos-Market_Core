package com.project.cuchosmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cuchosmarket.controllers.UserController;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testAddEmployee() throws Exception {
        // Arrange
        Long branchId = 1L;
        DtUser employee = new DtUser(1L, "carlos", "barrera", "carlos@", "123", "admin");

        // Simulate the behavior of the service
        doNothing().when(userService).addEmployee(branchId, employee);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/users/{branch_id}/employee", branchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Usuario añadido con exito."));

        // Verify that the service was called with the correct arguments
        verify(userService).addEmployee(branchId, employee);
    }
}



