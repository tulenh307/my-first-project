package com.jvit.service.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jvit.domain.GitLabMergeRequestWrapper;
import com.jvit.domain.MergeRequest;
import com.jvit.domain.search.GitLabMergeRequestFilter;
import com.jvit.external.HttpService;
import com.jvit.service.MergeRequestService;

@Service
public class MergeRequestServiceCustom implements MergeRequestService {

    @Autowired
    private HttpService httpService;

    private static final String GITLAB_MERGE_REQUEST_URL = "https://gitlab.com/api/v4/projects/projectId/merge_requests?access_token=";

    @Override
    public List<MergeRequest> getGitLabMergeRequestsByProject(GitLabMergeRequestFilter request, String token) {
        String url = GITLAB_MERGE_REQUEST_URL.replace("projectId", request.getProjectId().toString()).concat(token);
        url = getMergeRequestParam(url, request);
        ResponseEntity<GitLabMergeRequestWrapper[]> responses = httpService.get(url, null,
                GitLabMergeRequestWrapper[].class);
        List<MergeRequest> mergeRequests = new ArrayList<>();
        for (GitLabMergeRequestWrapper response : responses.getBody()) {
            MergeRequest mergeRequest = new MergeRequest();
            mergeRequest.setId(response.getId());
            mergeRequest.setIid(response.getIid());
            mergeRequest.setTitle(response.getTitle());
            mergeRequests.add(mergeRequest);
        }
        return mergeRequests;
    }

    private String getMergeRequestParam(String url, GitLabMergeRequestFilter request) {
        if (!StringUtils.isEmpty(request.getSource_branch())) {
            url = url.concat("&source_branch=").concat(request.getSource_branch());
        }

        if (!StringUtils.isEmpty(request.getTarget_branch())) {
            url = url.concat("&target_branch=").concat(request.getTarget_branch());
        }

        if (!StringUtils.isEmpty(request.getState())) {
            url = url.concat("&state=").concat(request.getState());
        }
        return url;
    }

}
