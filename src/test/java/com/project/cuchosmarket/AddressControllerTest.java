package com.project.cuchosmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.cuchosmarket.controllers.UserController;
import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.models.Address;
import com.project.cuchosmarket.services.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AddressControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController addressController;

    @Mock
    private AddressService addressService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
    }

    @Test
    @WithMockUser(username = "example@example.com")
    public void testGetAddress() throws Exception {
        // Arrange
        List<DtAddress> addresses = new ArrayList<>();
        addresses.add(new DtAddress(1L, "123 Main St", 4466, "Country", "f"));
        addresses.add(new DtAddress(2L, "456 First Ave", 5522, "Country", "e"));

        Mockito.when(addressService.getAddress(Mockito.anyString())).thenReturn(addresses);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/address"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)));


/*
    @Test
    public void testAddAddress() throws Exception {
        // Arrange
        String userEmail = "john@example.com";
        DtAddress dtAddress = new DtAddress();
        dtAddress.setStreet("123 Main St");
        dtAddress.setCity("City");
        dtAddress.setCountry("Country");

        // Act
        mockMvc.perform(post("/customer/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dtAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", is(false)))
                .andExpect(jsonPath("$.message", is("Direccion añadida con exito.")));

        // Assert
        Mockito.verify(addressService).addAddress(dtAddress, userEmail);
    }

    @Test
    public void testUpdateAddress() throws Exception {
        // Arrange
        String userEmail = "john@example.com";
        DtAddress dtAddress = new DtAddress();
        dtAddress.setId(1L);
        dtAddress.setStreet("123 Main St");
        dtAddress.setCity("City");
        dtAddress.setCountry("Country");

        // Act
        mockMvc.perform(put("/customer/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dtAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error", is(false)))
                .andExpect(jsonPath("$.message", is("Direccion actualizada con exito.")));

        // Assert
        Mockito.verify(addressService).updateAddress(userEmail, dtAddress);
    }
*/
    }
}





