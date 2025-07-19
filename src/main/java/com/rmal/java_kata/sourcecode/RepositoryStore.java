package com.rmal.java_kata.sourcecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


public class RepositoryStore {

    private static final Map<String, List<Repo>> USER_NAME_TO_REPOS_MAP = new HashMap<>();


    public static String add(String userName, Repo repo) {
        List<Repo> userRepos = USER_NAME_TO_REPOS_MAP.getOrDefault(userName, new ArrayList<>());
        checkForRepoNameExists(repo, userRepos);
        userRepos.add(repo);
        USER_NAME_TO_REPOS_MAP.put(userName, userRepos);
        return repo.name();
    }

    private static void checkForRepoNameExists(Repo repo, List<Repo> userRepos) {
        if (repoNameAlreadyExists(repo, userRepos)) {
            throw new IllegalArgumentException("repository already exist");
        }
    }

    private static boolean repoNameAlreadyExists(Repo repo, List<Repo> userRepos) {
        return userRepos.stream()
                .anyMatch(withNameOf(repo));
    }

    private static Predicate<Repo> withNameOf(Repo newRepo) {
        return repo -> repo.name().equals(newRepo.name());
    }

    public static List<Repo> findAll(String username) {
        return USER_NAME_TO_REPOS_MAP.get(username);
    }

    public static void clear() {
        USER_NAME_TO_REPOS_MAP.clear();
    }

}
