package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class GitHubAnalyzer {
    private static HttpURLConnection conn = null;
    private static ObjectMapper mapper = new ObjectMapper();

    public void analyze(String username) {
        System.out.printf("=== GitHub Profile: %s ===\n\n", username);
        analyzeProfile(username);
        analyzeRepositories(username);
        analyzeLanguages(username);
    }

    private static String getData(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Java-GitHubAnalyzer");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static void analyzeProfile(String username) {

        String urlStr = "https://api.github.com/users/" + username;
        String json = getData(urlStr);
        if(!isValid(json)){
            return;
        }
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Name : " + root.get("name").asText("No Name"));
        System.out.println("Bio : " + root.get("bio").asText("No Bio"));
        System.out.println("Followers : " + root.get("followers").asText("No Followers"));
        System.out.println("Following : " + root.get("following").asText("No Following"));
        System.out.println("Public Repos: +" + root.get("public_repos").asText("No Public Repos"));
    }

    public static void analyzeRepositories(String username) {
        String urlStr = "https://api.github.com/users/" + username + "/repos?per_page=100";
        String json = getData(urlStr);

        if(!isValid(json)){
            return;
        }

        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nTop 3 Starred Repos:");
        // to use root as stream
        StreamSupport.stream(root.spliterator(), false).sorted((r1, r2) -> {
            return r2.get("stargazers_count").asInt() - r1.get("stargazers_count").asInt();
        }).limit(3).forEach((repo) -> {
            System.out.printf("- %s (Stars : %s)\n", repo.get("name").asText("No Name"), repo.get("stargazers_count").asInt(0));
        });
        System.out.println();
    }

    public static void analyzeLanguages(String username) {

        String urlStr = "https://api.github.com/users/" + username + "/repos";
        String urlJson = getData(urlStr);
        if(!isValid(urlJson)){
            return;
        }

        JsonNode root = null;
        try {
            root = mapper.readTree(urlJson);
        } catch (JsonProcessingException e) {
        }
        Map<String,Long> map = new HashMap<>();

        StreamSupport.stream(root.spliterator(), false).forEach((repo) -> {
            JsonNode url = repo.get("languages_url");
            String str = url.asText();
            //System.out.println(str);  // url of repo

            String data = getData(str);   // date retrieved from repo languages Link
            if(!isValid(data)){
                return;
            }

            try {
                JsonNode languagesNode = mapper.readTree(data);
                Iterator<String> lang =  languagesNode.fieldNames();
                String langName;
                long val;
                while (lang.hasNext()) {
                    langName = lang.next();
                    val = map.getOrDefault(langName,0L);
                    map.put(langName, val + languagesNode.get(langName).asLong(0L));
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        final long sum = map.values().stream().reduce(0L, (a,b) -> a + b);
        System.out.println("Languages Used:");
        map.forEach((key, value) -> {
            double percentage = (double) value / sum * 100.0;
            System.out.printf("- %s: %.2f%%\n", key, percentage);
        });
        System.out.println();
    }
    private static boolean isValid(String json){
        if(json == null || json.trim().isEmpty() || json.isBlank()){
            return false;
        }
        return true;
    }
}
