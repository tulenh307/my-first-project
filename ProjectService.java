package com.jvit.service;

import java.util.List;

import com.jvit.domain.Project;

public interface ProjectService {

    List<Project> getAllGitLabProjects(String token);
}
