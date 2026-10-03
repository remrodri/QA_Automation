package com.automation.core.jira;

public final class JiraConfig {
    private final String baseUrl;
    private final String email;
    private final String apiToken;
    private final String projectKey;
    private final String issueType;
    private final boolean publish;

    private JiraConfig(String baseUrl, String email, String apiToken, String projectKey,
            String issueType, boolean publish) {
        this.baseUrl = trimTrailingSlash(baseUrl);
        this.email = email;
        this.apiToken = apiToken;
        this.projectKey = projectKey;
        this.issueType = issueType;
        this.publish = publish;
    }

    public static JiraConfig load() {
        String mode = setting("jira.mode", "JIRA_MODE", "dry-run");
        return new JiraConfig(
                setting("jira.baseUrl", "JIRA_BASE_URL", ""),
                setting("jira.email", "JIRA_EMAIL", ""),
                setting("jira.apiToken", "JIRA_API_TOKEN", ""),
                setting("jira.projectKey", "JIRA_PROJECT_KEY", ""),
                setting("jira.issueType", "JIRA_ISSUE_TYPE", "Bug"),
                "publish".equalsIgnoreCase(mode));
    }

    public void validateForPublish() {
        require(baseUrl, "JIRA_BASE_URL");
        require(email, "JIRA_EMAIL");
        require(apiToken, "JIRA_API_TOKEN");
        require(projectKey, "JIRA_PROJECT_KEY");
    }

    public String getBaseUrl() { return baseUrl; }
    public String getEmail() { return email; }
    public String getApiToken() { return apiToken; }
    public String getProjectKey() { return projectKey; }
    public String getIssueType() { return issueType; }
    public boolean isPublish() { return publish; }

    private static String setting(String property, String environment, String fallback) {
        String value = System.getProperty(property);
        if (value == null || value.trim().isEmpty()) value = System.getenv(environment);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private static String trimTrailingSlash(String value) {
        if (value == null) return "";
        while (value.endsWith("/")) value = value.substring(0, value.length() - 1);
        return value;
    }

    private static void require(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Falta la variable requerida para publicar en JIRA: " + name);
        }
    }
}
