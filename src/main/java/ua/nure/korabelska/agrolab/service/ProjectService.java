package ua.nure.korabelska.agrolab.service;

import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;

public interface ProjectService {

    Project createProject(SaveProjectDto projectDto) throws UserNotFoundException;
    Boolean deleteProjectById(Long id);
    Project updateProject(UpdateProjectDto saveProjectDto, Long Id) throws UserNotFoundException;
    Project findProjectById(Long id);

}
