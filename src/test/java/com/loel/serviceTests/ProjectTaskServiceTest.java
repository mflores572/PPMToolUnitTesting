package com.loel.serviceTests;


import com.loel.domain.Backlog;
import com.loel.domain.Project;
import com.loel.domain.ProjectTask;
import com.loel.repositories.ProjectRepository;
import com.loel.repositories.ProjectTaskRepository;
import com.loel.services.ProjectTaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectTaskServiceTest {
    @Autowired
    private ProjectTaskService projectTaskService;

    @MockBean
    private ProjectTaskRepository projectTaskRepository;

    @MockBean
    private ProjectRepository projectRepository;

    private String pSequence;
    private String ptSummary;
    private long id;
    private ProjectTask myPT;

    private String pSequence_two;
    private String ptSummary_two;
    private long id_two;
    private ProjectTask myPT_two;

    //gonna work with the same user, backlog, and project
    private String myPID;
    private String myUsername;
    private Project myProject;
    private Backlog myBacklog;


    @BeforeEach
    public void contextLoads() {
        /* Arrange */
        //Just some data to fill the Project Task with
        myPID = "TSTP1";
        pSequence = "TSTP1-1";
        ptSummary = "Great summary!";
        id = 1;

        pSequence_two = "TSTP1-2";
        ptSummary_two = "What a blah summary!";
        id_two = 2;

        //Set up the Project Task
        myPT = new ProjectTask();
        myPT.setProjectIdentifier(myPID);
        myPT.setProjectSequence(pSequence);
        myPT.setId(id);
        myPT.setSummary(ptSummary);

        //Set up another Project Task within the same project and username as the one above
        myPT_two = new ProjectTask();
        myPT_two.setProjectIdentifier(myPID);
        myPT_two.setProjectSequence(pSequence_two);
        myPT_two.setId(id_two);
        myPT_two.setSummary(ptSummary_two);


        myUsername = "Test@mytest.com";
        myProject = new Project((long) 1, "My Project Name", myPID, "My Project Description", myUsername);
        myBacklog = new Backlog();
        myBacklog.setPTSequence(1);
        myProject.setBacklog(myBacklog); //very important!!!!
    }

    @Test
    public void testaddProjectTask() {
        /* Act */
        when(projectRepository.findByProjectIdentifier(myPID)).thenReturn(myProject);
        when(projectTaskRepository.save(myPT)).thenReturn(myPT);

        /* Assert */
        ProjectTask ptFromService = projectTaskService.addProjectTask(myPID, myPT, myUsername);
        Assertions.assertEquals(ptFromService, myPT);
    }

    @Test
    public void testFindBacklogById() {
        /* Act */
        when(projectRepository.findByProjectIdentifier(myPID)).thenReturn(myProject);
        when(projectTaskRepository.findByProjectIdentifierOrderByPriority(myPID)).thenReturn(Stream
                .of(myPT, myPT_two).collect(Collectors.toList()));

        /* Assert */
        Iterable<ProjectTask> ptFromService = projectTaskService.findBacklogById(myPID, myUsername);

        for (ProjectTask u : ptFromService)
            System.out.println(u.getSummary());

        //Assertions.assertEquals(ptFromService, myPT);

    }

    @Test
    public void testFindPTByProjectSequence() {
        /* Act */
        when(projectRepository.findByProjectIdentifier(myPID)).thenReturn(myProject);
        when(projectTaskRepository.findByProjectSequence(pSequence_two)).thenReturn(myPT_two);

        /* Assert */
        ProjectTask ptFromService = projectTaskService.findPTByProjectSequence(myPID, pSequence_two, myUsername);
        Assertions.assertEquals(ptFromService, myPT_two);
    }

    @Test
    public void testUpdateByProjectSequence() {
        /* Act */
        when(projectRepository.findByProjectIdentifier(myPID)).thenReturn(myProject);
        when(projectTaskRepository.findByProjectSequence(pSequence)).thenReturn(myPT);
        when(projectTaskRepository.save(myPT)).thenReturn(myPT);
        myPT.setSummary("Updated Summary");
        when(projectTaskRepository.save(myPT)).thenReturn(myPT);

        /* Assert */
        ProjectTask ptFromService = projectTaskService.findPTByProjectSequence(myPID, pSequence, myUsername);
        Assertions.assertEquals(ptFromService, myPT);
        Assertions.assertEquals(ptFromService.getSummary(), myPT.getSummary());

    }

} //end of ProjectTaskServiceTest.java