package com.foodtrack.spring.springboot_application.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Security configuration")
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should reject profile access without JWT")
    void shouldRejectProfileAccessWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should reject public registration endpoint without JWT")
    void shouldRejectPublicRegistrationEndpointWithoutJwt() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Security Test",
                                  "email": "security-test@example.com",
                                  "password": "Admin123!"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should reject protected table creation without JWT")
    void shouldRejectProtectedTableCreationWithoutJwt() throws Exception {
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tableNumber": 99
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}
