package com.automation.core.jira;

import java.nio.file.Path;

public final class JiraSubmissionResult {
    private final boolean published;
    private final String issueKey;
    private final Path draftPath;

    private JiraSubmissionResult(boolean published, String issueKey, Path draftPath) {
        this.published = published;
        this.issueKey = issueKey;
        this.draftPath = draftPath;
    }

    public static JiraSubmissionResult published(String issueKey) {
        return new JiraSubmissionResult(true, issueKey, null);
    }

    public static JiraSubmissionResult draft(Path draftPath) {
        return new JiraSubmissionResult(false, null, draftPath);
    }

    public boolean isPublished() { return published; }
    public String getIssueKey() { return issueKey; }
    public Path getDraftPath() { return draftPath; }
}
