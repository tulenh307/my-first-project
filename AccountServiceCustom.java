package com.jvit.service.custom;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jvit.domain.Account;
import com.jvit.domain.GitLabAccountWrapper;
import com.jvit.domain.GitLabTokenWrapper;
import com.jvit.domain.search.GitLabTokenRequest;
import com.jvit.external.HttpService;
import com.jvit.service.AccountService;

@Service
public class AccountServiceCustom implements AccountService {

    private static final String GITLAB_TOKEN_URL = "https://gitlab.com/oauth/token";
    private static final String GITLAB_USER_URL = "https://gitlab.com/api/v4/user/?access_token=";

    @Autowired
    private HttpService httpService;

    @Override
    public Account getGitLabAccount(GitLabTokenRequest request) throws AuthenticationException {
        String token = getTokenGitLab(request);
        String url = GITLAB_USER_URL.concat(token);
        ResponseEntity<GitLabAccountWrapper> response = httpService.get(url, null, GitLabAccountWrapper.class);

        if (response.getBody() != null) {
            Account account = new Account();
            account.setAccessToken(token);
            account.setId(response.getBody().getId());
            account.setUsername(response.getBody().getUsername());
            account.setEmail(response.getBody().getEmail());
            account.setName(response.getBody().getName());
            return account;
        }
        return null;
    }

    private String getTokenGitLab(GitLabTokenRequest request) throws AuthenticationException {
        ResponseEntity<GitLabTokenWrapper> response = httpService.post(GITLAB_TOKEN_URL, request,
                GitLabTokenWrapper.class);
        if (response.getBody() != null) {
            return response.getBody().getAccessToken();
        }
        throw new AuthenticationException("Username/Password does not match");
    }
}
