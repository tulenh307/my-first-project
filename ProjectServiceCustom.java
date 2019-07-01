package com.jvit.service.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jvit.domain.GitLabProjectWrapper;
import com.jvit.domain.Project;
import com.jvit.external.HttpService;
import com.jvit.service.ProjectService;

@Service
public class ProjectServiceCustom implements ProjectService {

    @Autowired
    private HttpService httpService;

    private static final String GITLAB_PROJECT_URL = "https://gitlab.com/api/v4/projects?membership=true&access_token=";

    @Override
    public List<Project> getAllGitLabProjects(String token) {
        String url = GITLAB_PROJECT_URL.concat(token);
        ResponseEntity<GitLabProjectWrapper[]> responses = httpService.get(url, null, GitLabProjectWrapper[].class);
        List<Project> projects = new ArrayList<>();

        for (GitLabProjectWrapper response : responses.getBody()) {
            Project project = new Project();
            project.setId(response.getId());
            project.setName(response.getName());
            projects.add(project);
        }

        return projects;
    }

}
