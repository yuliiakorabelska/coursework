package ua.nure.korabelska.agrolab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ua.nure.korabelska.agrolab.dto.SaveProjectDto;
import ua.nure.korabelska.agrolab.dto.UpdateProjectDto;
import ua.nure.korabelska.agrolab.exception.UserNotFoundException;
import ua.nure.korabelska.agrolab.model.Project;
import ua.nure.korabelska.agrolab.model.User;
import ua.nure.korabelska.agrolab.repository.ProjectRepository;
import ua.nure.korabelska.agrolab.repository.UserRepository;
import ua.nure.korabelska.agrolab.service.ProjectService;

import java.util.HashSet;
import java.util.Set;

public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;

    UserRepository userRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Project createProject(SaveProjectDto saveProjectDto) throws UserNotFoundException {
        Project project = new Project();
        User manager = userRepository.findById(saveProjectDto.getManagerId())
                .orElseThrow(() -> new UserNotFoundException(saveProjectDto.getManagerId()));
        project.setManager(manager);

        project.setMembers(collectMembers(saveProjectDto.getMembersId()));

        project.setName(saveProjectDto.getName());

        return project;
    }

    @Override
    public Boolean deleteProjectById(Long id) {
        Boolean exists = projectRepository.existsById(id);
        if(exists) {
            projectRepository.deleteById(id);
        }
        return exists;
    }

    @Override
    public Project updateProject(UpdateProjectDto updateProjectDto, Long id) throws UserNotFoundException {
        if(projectRepository.existsById(id)){
            Project project = projectRepository.findById(id).orElse(null);
            project.setMembers(collectMembers(updateProjectDto.getMembersId()));
            project.setName(updateProjectDto.getName());
            return project;
        }
        return null;
    }

    @Override
    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    private Set<User> collectMembers(Set<Long> membersId) throws UserNotFoundException {
        Set<User> users = new HashSet<>();

        for (Long memberId : membersId) {
            users.add(userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException(memberId)));
        }
        return users;
    }

}
