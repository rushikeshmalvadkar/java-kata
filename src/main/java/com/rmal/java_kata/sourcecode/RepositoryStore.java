package com.rmal.java_kata.sourcecode;

import com.rmal.java_kata.sourcecode.exceptions.RepoNotFoundException;

import java.util.*;
import java.util.function.Predicate;


public class RepositoryStore {

    private static final Map<String, List<Repo>> USER_NAME_TO_REPOS_MAP = new HashMap<>();


    public static String add(String userName, Repo repo) {
        List<Repo> userRepos = USER_NAME_TO_REPOS_MAP.getOrDefault(userName, new ArrayList<>());
        checkForRepoNameExists(repo, userRepos);
        userRepos.add(repo);
        USER_NAME_TO_REPOS_MAP.put(userName, userRepos);
        return repo.getName();
    }

    public static void checkForRepoNameExists(Repo repo, List<Repo> userRepos) {
        if (repoNameAlreadyExists(repo, userRepos)) {
            throw new IllegalArgumentException("repository already exist");
        }
    }

    private static boolean repoNameAlreadyExists(Repo repo, List<Repo> userRepos) {
        return userRepos.stream()
                .anyMatch(withNameOf(repo));
    }

    private static Predicate<Repo> withNameOf(Repo newRepo) {
        return repo -> repo.getName().equals(newRepo.getName());
    }

    public static List<Repo> findAll(String username) {
        return USER_NAME_TO_REPOS_MAP.get(username);
    }

    public static void clear() {
        USER_NAME_TO_REPOS_MAP.clear();
    }

    public static void renameRepo(String oldReoName, String newRepoName, String userName) {
        List<Repo> userRepo = findAll(userName);
        userRepo.forEach( repo ->
        {
            if(repo.getName().equals(oldReoName)){
                repo.setName(newRepoName);USER_NAME_TO_REPOS_MAP
                .put(userName, userRepo);
            }
        });

    }


    public static Repo findRepoBy(String repoName, String userName) {
        List<Repo> userRepositories = findAll(userName);
        return userRepositories
                .stream()
                .filter(repository -> repository.getName().equals(repoName))
                .findFirst().orElseThrow(() -> new RepoNotFoundException("repoNotFound"));

    }
}
