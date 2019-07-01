package com.jvit.service.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jvit.domain.Branch;
import com.jvit.domain.GitLabBranchWrapper;
import com.jvit.domain.search.GitLabBranchRequest;
import com.jvit.external.HttpService;
import com.jvit.service.BranchService;

@Service
public class BranchServiceCustom implements BranchService {

    @Autowired
    private HttpService httpService;

    private static final String GITLAB_BRANCH_URL = "https://gitlab.com/api/v4/projects/projectId/repository/branches?access_token=";

    @Override
    public List<Branch> getGitLabBranchs(GitLabBranchRequest request, String token) {
        String url = GITLAB_BRANCH_URL.replace("projectId", request.getProjectId().toString()).concat(token);
        ResponseEntity<GitLabBranchWrapper[]> responses = httpService.get(url, null, GitLabBranchWrapper[].class);
        List<Branch> branchs = new ArrayList<>();
        for (GitLabBranchWrapper response : responses.getBody()) {
            Branch branch = new Branch();
            branch.setName(response.getName());
            branchs.add(branch);
        }
        return branchs;
    }

}
