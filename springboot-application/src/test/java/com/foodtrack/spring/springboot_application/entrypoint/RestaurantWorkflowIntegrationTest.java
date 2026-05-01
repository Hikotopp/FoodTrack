package com.foodtrack.spring.springboot_application.entrypoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Restaurant workflow")
class RestaurantWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create employee, process an order and expose it in sales history")
    void shouldProcessCompleteRestaurantWorkflow() throws Exception {
        String adminToken = login("admin@test.local", "Admin123!");

        String employeeEmail = "workflow.employee@example.com";
        mockMvc.perform(post("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Workflow Employee",
                                  "email": "%s",
                                  "password": "Employee123!",
                                  "role": "EMPLOYEE"
                                }
                                """.formatted(employeeEmail)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(employeeEmail))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));

        String employeeToken = login(employeeEmail, "Employee123!");

        JsonNode tables = readJson(mockMvc.perform(get("/api/tables")
                        .header(HttpHeaders.AUTHORIZATION, bearer(employeeToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andReturn());
        long tableId = tables.get(0).get("id").asLong();

        JsonNode dashboard = readJson(mockMvc.perform(get("/api/tables/{tableId}", tableId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(employeeToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menuItems[0].id").exists())
                .andReturn());
        long menuItemId = dashboard.get("menuItems").get(0).get("id").asLong();

        mockMvc.perform(post("/api/tables/{tableId}/order-lines", tableId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(employeeToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "menuItemId": %d,
                                  "quantity": 2
                                }
                                """.formatted(menuItemId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentOrder.lines[0].quantity").value(2))
                .andExpect(jsonPath("$.currentOrder.total").isNumber());

        mockMvc.perform(post("/api/tables/{tableId}/close-order", tableId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(employeeToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentOrder").doesNotExist());

        mockMvc.perform(get("/api/sales/history")
                        .header(HttpHeaders.AUTHORIZATION, bearer(employeeToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CLOSED"))
                .andExpect(jsonPath("$[0].createdByName").value("Workflow Employee"))
                .andExpect(jsonPath("$[0].lines[0].quantity").value(2));
    }

    private String login(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn();

        return readJson(result).get("token").asText();
    }

    private JsonNode readJson(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
