# 🔍 GitHub Analyzer

A Java CLI tool that analyzes GitHub profiles using the GitHub REST API and Jackson library.

![Java](https://img.shields.io/badge/Java-17+-orange)
![Jackson](https://img.shields.io/badge/Jackson-2.17.2-green)

## ✨ Features

- 📊 Profile info (bio, followers, repos)
- ⭐ Top 3 starred repositories  
- 💻 Language breakdown with percentages

## 📦 Requirements

- Java 17+
- Maven 3.8+
- Internet connection

## 🚀 Quick Start

**Clone & Build:**
```bash
git clone https://github.com/your-username/GitHubAnalyzer.git
cd GitHubAnalyzer
mvn clean package
```

**Run:**
```bash
java -cp target/GitHubAnalyzer-1.0-SNAPSHOT.jar org.example.Main <username>
```

**Example:**
```bash
java -cp target/GitHubAnalyzer-1.0-SNAPSHOT.jar org.example.Main torvalds
```

## 📤 Sample Output

```
=== GitHub Profile: torvalds ===

Name: Linus Torvalds
Bio: Software Engineer
Followers: 200000
Following: 0
Public Repos: 6

Top 3 Starred Repos:
- linux (Stars: 150000)
- git (Stars: 45000)
- subsurface (Stars: 5000)

Languages Used:
- C: 80.23%
- Assembly: 15.44%
- Shell: 4.33%
```

## 🛠️ How It Works

1. **HTTP Request** - Uses `HttpURLConnection` to call GitHub API
2. **JSON Parsing** - Jackson `ObjectMapper` parses responses
3. **Data Processing** - Java Streams sort and aggregate data
4. **Output** - Formatted console display

## 📁 Project Structure

```
src/main/java/org/example/
├── GitHubAnalyzer.java    # Core logic
└── Main.java              # CLI entry point
pom.xml                    # Dependencies
```

## 📚 Dependencies

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.2</version>
</dependency>
```

## 🔗 GitHub API Endpoints

| Endpoint | Purpose |
|----------|---------|
| `/users/{username}` | User profile |
| `/users/{username}/repos` | List repositories |
| `/repos/{owner}/{repo}/languages` | Language stats |

---

## 🎯 Key Concepts Demonstrated

- REST API integration
- JSON parsing (Jackson library)
- Stream API for data processing
- Error handling and validation
- HTTP request/response handling

## 📝 Code Highlights

**JSON Parsing:**
```java
JsonNode root = mapper.readTree(json);
String name = root.get("name").asText();
```

**Stream Processing:**
```java
StreamSupport.stream(root.spliterator(), false)
    .sorted((r1, r2) -> r2.get("stargazers_count").asInt() - r1.get("stargazers_count").asInt())
    .limit(3)
    .forEach(repo -> System.out.println(repo.get("name").asText()));
```

**Language Aggregation:**
```java
Map<String, Long> languages = new HashMap<>();
// Aggregate bytes of code per language
// Calculate percentages
```

## 🚀 Future Ideas

- [ ] Export to JSON/CSV
- [ ] Compare multiple users
- [ ] Contribution graphs
- [ ] Repository activity timeline

## 👨‍💻 Author

**Mostafa Nady**

A practical example of REST API integration and JSON processing in Java.

---

⭐ **Star this repo** if you found it useful!

**Try it:** `java -cp target/GitHubAnalyzer-1.0-SNAPSHOT.jar org.example.Main octocat`
