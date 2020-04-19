package ua.nure.korabelska.agrolab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.service.ProjectService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/project")
@Slf4j
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProject(@PathVariable Long id) {
        return ResponseEntity.ok().body(projectService.findProjectById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createProject(@Valid @RequestBody SaveProjectDto saveProjectDto,
                                                BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        log.info("new project to be created : {} ", saveProjectDto);
        try {
            Project project = projectService.createProject(saveProjectDto);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateProject(@PathVariable Long id,
                                                @Valid @RequestBody UpdateProjectDto updateProjectDto,
                                                BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }

        log.info("updating project by id : {}", id);
        try {
            Project project = projectService.updateProject(updateProjectDto, id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProject(@PathVariable Long id) {
        log.info("deleting project by id : {}", id);
        if (projectService.deleteProjectById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
