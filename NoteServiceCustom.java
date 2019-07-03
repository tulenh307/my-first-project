package com.jvit.service.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jvit.domain.GitLabNoteWrapper;
import com.jvit.domain.Note;
import com.jvit.domain.search.GitLabNoteRequest;
import com.jvit.external.HttpService;
import com.jvit.service.NoteService;

@Service
public class NoteServiceCustom implements NoteService {

    private static final String GITLAB_NOTE_URL = "https://gitlab.com/api/v4/projects/projectId/merge_requests/mergeRequestIid/notes?access_token=";

    @Autowired
    private HttpService httpService;

    @Override
    public List<Note> getGitLabNotes(GitLabNoteRequest request, String token) {
        String url = GITLAB_NOTE_URL.replace("projectId", request.getProjectId().toString())
                .replace("mergeRequestIid", request.getMergeRequestIid().toString()).concat(token);
        ResponseEntity<GitLabNoteWrapper[]> responses = httpService.get(url, null, GitLabNoteWrapper[].class);
        List<Note> notes = new ArrayList<>();
        for (GitLabNoteWrapper response : responses.getBody()) {
            if (!response.isSystem()) {
                Note note = new Note();
                note.setId(response.getId());
                note.setContent(response.getContent());
                notes.add(note);
            }
        }
        return notes;
    }

}
