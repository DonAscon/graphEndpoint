package graphEndpoint.dataConnection.controller;

import graphEndpoint.MainApp;
import graphEndpoint.dataConnection.domain.Customer;
import graphEndpoint.dataConnection.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = CustomerController.class)
@ContextConfiguration(classes = MainApp.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    CustomerRepository customerRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    public void shouldReturn200WhenSendingRequestToControllerWithRoleUser() throws Exception {
        given(customerRepository.findAll()).willReturn(new ArrayList<Customer>());
        mockMvc.perform(get("/api/customer/all")).andExpect(status().isOk());
    }

}