package com.automation.core.jira;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class JiraClient {
    private final JiraConfig config;
    private final ObjectMapper mapper;

    public JiraClient(JiraConfig config) {
        this.config = config;
        this.mapper = new ObjectMapper();
    }

    public ObjectNode buildPayload(JiraIssue issue) {
        ObjectNode root = mapper.createObjectNode();
        ObjectNode fields = root.putObject("fields");
        String projectKey = config.getProjectKey().isEmpty() ? "PROJECT_KEY" : config.getProjectKey();
        fields.putObject("project").put("key", projectKey);
        fields.put("summary", issue.getSummary());
        fields.putObject("issuetype").put("name", config.getIssueType());

        ObjectNode description = fields.putObject("description");
        description.put("type", "doc");
        description.put("version", 1);
        ArrayNode content = description.putArray("content");
        ObjectNode paragraph = content.addObject();
        paragraph.put("type", "paragraph");
        paragraph.putArray("content").addObject()
                .put("type", "text")
                .put("text", issue.getDescription());

        ArrayNode labels = fields.putArray("labels");
        for (String label : issue.getLabels()) labels.add(label);
        return root;
    }

    public String createIssue(JiraIssue issue) throws IOException {
        config.validateForPublish();
        byte[] body = mapper.writeValueAsBytes(buildPayload(issue));
        HttpURLConnection connection = (HttpURLConnection) new URL(
                config.getBaseUrl() + "/rest/api/3/issue").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        String credentials = config.getEmail() + ":" + config.getApiToken();
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encoded);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(body);
        }

        int status = connection.getResponseCode();
        String response = read(status >= 200 && status < 300
                ? connection.getInputStream() : connection.getErrorStream());
        if (status != 201) {
            throw new IOException("JIRA devolvió HTTP " + status + ": " + response);
        }
        JsonNode responseJson = mapper.readTree(response);
        return responseJson.path("key").asText();
    }

    ObjectMapper getMapper() { return mapper; }

    private static String read(InputStream stream) throws IOException {
        if (stream == null) return "";
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) text.append(line);
        }
        return text.toString();
    }
}
