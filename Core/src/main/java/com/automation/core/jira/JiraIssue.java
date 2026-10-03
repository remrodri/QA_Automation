package com.automation.core.jira;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class JiraIssue {
    private final String summary;
    private final String description;
    private final List<String> labels;

    public JiraIssue(String summary, String description, String... labels) {
        if (summary == null || summary.trim().isEmpty()) throw new IllegalArgumentException("summary es obligatorio");
        if (description == null || description.trim().isEmpty()) throw new IllegalArgumentException("description es obligatorio");
        this.summary = summary.trim();
        this.description = description.trim();
        this.labels = labels == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(Arrays.asList(labels));
    }

    public String getSummary() { return summary; }
    public String getDescription() { return description; }
    public List<String> getLabels() { return labels; }
}
