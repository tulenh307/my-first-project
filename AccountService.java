package com.jvit.service;

import javax.security.sasl.AuthenticationException;

import com.jvit.domain.Account;
import com.jvit.domain.search.GitLabTokenRequest;

public interface AccountService {

    Account getGitLabAccount(GitLabTokenRequest request) throws AuthenticationException;
}
