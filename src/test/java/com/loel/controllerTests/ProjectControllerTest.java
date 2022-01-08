package com.loel.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loel.domain.Project;
import com.loel.domain.User;
import com.loel.repositories.UserRepository;
import com.loel.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {
    ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    ProjectService projectService;

    public ProjectControllerTest() {
    }

    @BeforeEach
    public void contextLoads() {
        userRepository.save(new User(1l, "john_doe@gmail.com", "John Doe", "password"));
        userRepository.save(new User(2l, "jane_doe@gmail.com", "Jane Doe", "password"));

        mapper = new ObjectMapper();
    }

    @Test
    public void testGetAllProjects() throws Exception {
        ArrayList<Project> arr = new ArrayList<>();
        Project myProject = new Project((long) 1, "no-name", "TSTP", "Blah", "john_doe@gmail.com");
        Project yourProject = new Project((long) 1, "name", "TSTP1", "Blah blah", "john_doe@gmail.com");

        arr.add(myProject);
        arr.add(yourProject);

        when(projectService.findAllProjects("john_doe@gmail.com")).thenReturn(arr);

        mockMvc.perform(get("/api/project/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJmdWxsTmFtZSI6Im1hcmlvIGZsb3JlcyIsImlkIjoiMTEiLCJleHAiOjE2NDA4MDI0NjQsImlhdCI6MTY0MDc5ODg2NCwidXNlcm5hbWUiOiJtYXJpb2ZmZkBtYXJpb2YuY29tIn0.OK9eXjroj-x-EEU1j_-Ip-4KzRpgjRYAPj9GZS-Rd3bLkPVSELH2_lhQbVO8uSS6zr-fXo9Hhy9gBLv2ulktzw"))
                .andExpect(jsonPath("$.username").value("You got something wrong pal"))
                .andExpect(jsonPath("$.password").value("Yep something is wrong bud"));
    }
}