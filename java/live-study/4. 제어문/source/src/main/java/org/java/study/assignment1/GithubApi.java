package org.java.study.assignment1;

public class GithubApi {

    public static void main(String[] args) {

        GitHub gitHub = new GitHubBuilder().withOAuthToken("").build();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");

        List<GHIssue> issues = repository.getIssues(GHIssueState.ALL);
        Map<String, Integer> users = new HashMap<>();
        for (GHIssue issue : issues) {
            PagedIterable<GHIssueComment> comments = issue.listComments();
            for (GHIssueComment comment : comments) {
                String userName = comment.getUser().getName();
                if (Objects.isNull(userName)) continue;
                users.put(userName, users.getOrDefault(userName, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> user : users.entrySet()) {
            String value = String.format("%.2f", (user.getValue() / (float) 18) * 100);
            System.out.println(user.getKey() + ": " + value + "%");
        }

    }

}
