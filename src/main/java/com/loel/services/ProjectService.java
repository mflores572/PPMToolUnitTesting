package com.loel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loel.domain.Backlog;
import com.loel.domain.Project;
import com.loel.domain.User;
import com.loel.exceptions.ProjectIDException;
import com.loel.exceptions.ProjectNotFoundException;
import com.loel.repositories.BacklogRepository;
import com.loel.repositories.ProjectRepository;
import com.loel.repositories.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private UserRepository userRepository;

	public Project saveOrUpdateProject(Project project, String username) {
		if (project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("This Project is not in your control boss!");
			} else if (existingProject == null) {
				throw new ProjectNotFoundException("This Project with ID: '" + project.getProjectIdentifier()
						+ "' only exists in an Alternate Universe");
			}
		}
		try {
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}

			if (project.getId() != null) {
				project.setBacklog(
						backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}

			return projectRepository.save(project);

		} catch (Exception e) {
			throw new ProjectIDException("Project ID '" + project.getProjectIdentifier().toUpperCase()
					+ "' has been used try again sport!!");
		}

	}

	public Project findProjectByIdentifier(String projectId, String username) {

		// Only want to return the project if the user looking for it is the owner

		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

		if (project == null) {
			throw new ProjectIDException("Project ID '" + projectId + "' only exists in an Alternate Reality");

		}
		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Quit snooping this isn't your Project");
		}

		return project;
	}

	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}

	public void deleteProjectByIdentifier(String projectid, String username) {

		projectRepository.delete(findProjectByIdentifier(projectid, username));
	}

}