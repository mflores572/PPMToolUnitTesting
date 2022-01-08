package com.loel.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import com.loel.domain.Project;
import com.loel.services.MapValidationErrorService;
import com.loel.services.ProjectService;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@PostMapping("")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result,
			Principal principal) {

		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
		if (errorMap != null)
			return errorMap;

		Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
		return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {

		Project project = projectService.findProjectByIdentifier(projectId, principal.getName());

		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<Project> getAllProjects(Principal principal) {
		return projectService.findAllProjects(principal.getName());
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
		projectService.deleteProjectByIdentifier(projectId, principal.getName());

		return new ResponseEntity<String>("You deleted the Project with ID: '" + projectId + "'", HttpStatus.OK);
	}
}