package com.project.cuchosmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cuchosmarket.controllers.UserController;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.services.UserService;
import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.repositories.CustomerRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private MockMvc mockMvc;

    @InjectMocks
    private UserController lookupController;

    @Mock
    UserService userService;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(lookupController).build();
    }

    @Test
    void whenSubmitUser_thenUserIdIsGenerated() throws Exception {
        /*User newUser = new User();
        newUser.setName("My Refactor");

        User result = new User();
        result.setName("My Refactor");
        UUID id = UUID.randomUUID();
        result.setId(id);
*/
      //  Customer cust = new Customer();
      //  cust.setFirstName("carlos");
        DtCustomer customer = new DtCustomer(1l,"carlos","barrera","carlos@","123","admin");

        //Mockito.when(userService.addCustomer(Mockito.any(DtCustomer.class))).thenReturn(customer);
        Mockito.verify(userService).addCustomer(Mockito.any(DtCustomer.class));

        this.mockMvc
                .perform(post("/add-user").content(asJsonString(customer)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(equalTo(id.toString()))))
                .andReturn();
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


