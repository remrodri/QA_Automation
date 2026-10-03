package com.automation.core.jira;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;

public final class JiraBugReporter {
    private final JiraConfig config;
    private final JiraClient client;
    private final Path draftDirectory;

    public JiraBugReporter(JiraConfig config, Path draftDirectory) {
        this.config = config;
        this.client = new JiraClient(config);
        this.draftDirectory = draftDirectory;
    }

    public static JiraBugReporter configured() {
        return new JiraBugReporter(JiraConfig.load(), Paths.get("target", "jira-dry-run"));
    }

    public JiraSubmissionResult submit(JiraIssue issue) throws IOException {
        if (config.isPublish()) {
            return JiraSubmissionResult.published(client.createIssue(issue));
        }
        Files.createDirectories(draftDirectory);
        Path draft = draftDirectory.resolve(fileName(issue.getSummary()) + ".json");
        client.getMapper().writerWithDefaultPrettyPrinter()
                .writeValue(draft.toFile(), client.buildPayload(issue));
        return JiraSubmissionResult.draft(draft.toAbsolutePath());
    }

    private static String fileName(String summary) {
        String ascii = Normalizer.normalize(summary, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        String normalized = ascii.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        normalized = normalized.replaceAll("^-|-$", "");
        return normalized.isEmpty() ? "jira-issue" : normalized;
    }
}
