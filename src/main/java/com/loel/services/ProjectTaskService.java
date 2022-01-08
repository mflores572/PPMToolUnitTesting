package com.loel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loel.domain.Backlog;
import com.loel.domain.Project;
import com.loel.domain.ProjectTask;
import com.loel.exceptions.ProjectNotFoundException;
import com.loel.repositories.BacklogRepository;
import com.loel.repositories.ProjectRepository;
import com.loel.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

		// PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog(); // backlogRepository.findByProjectIdentifier(projectIdentifier);
		// set the bl to pt
		System.out.println(backlog);
		projectTask.setBacklog(backlog);
		// we want our project sequence to be like this: IDPRO-1 IDPRO-2 ...100 101
		Integer BacklogSequence = backlog.getPTSequence();
		// Update the BackLog SEQUENCE
		BacklogSequence++;

		backlog.setPTSequence(BacklogSequence);

		// Add Sequence to Project Task
		projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);

		// INITIAL priority when priority null

		// INITIAL status when status is null
		if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}

		// Fix bug with priority in Spring Boot Server, needs to check null first
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) { // In the future we need
																					// projectTask.getPriority()== 0 to
																					// handle the form
			projectTask.setPriority(3);
		}

		return projectTaskRepository.save(projectTask);
	}

	public Iterable<ProjectTask> findBacklogById(String id, String username) {

		projectService.findProjectByIdentifier(id, username);

		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

		// make sure we are searching on an existing backlog
		projectService.findProjectByIdentifier(backlog_id, username);

		// make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

		if (projectTask == null) {
			throw new ProjectNotFoundException(
					"This Project Task '" + pt_id + "' aren't the droids you are looking for");
		}

		// make sure that the backlog/project id in the path corresponds to the right
		// project
		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException(
					"This Project Task '" + pt_id + "' only exists in a different reality for Project: '" + backlog_id);
		}

		return projectTask;
	}

	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id,
			String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

		projectTask = updatedTask;

		return projectTaskRepository.save(projectTask);
	}

	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		projectTaskRepository.delete(projectTask);
	}

}