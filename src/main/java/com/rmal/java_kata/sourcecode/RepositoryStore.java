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
        return repo.name();
    }

    public static List<Repo> searchRepo(String searchText, String userName) {
        List<Repo> userRepositories = findReposOf(userName);
        return userRepositories.stream()
                .filter(repo -> repo.name().contains(searchText))
                .toList();
    }

    public static void deleteRepo(String repoName, String userName) {
        List<Repo> userRepos = findReposOf(userName);
        Repo repoToBeDeleted = findRepoBy(repoName, userName);
        userRepos.remove(repoToBeDeleted);
        USER_NAME_TO_REPOS_MAP.put(userName, userRepos);
    }

    public static void checkForRepoNameExists(Repo repo, List<Repo> userRepos) {
        if (repoNameAlreadyExists(repo, userRepos)) {
            throw new IllegalArgumentException("repository already exist");
        }
    }

    public static List<Repo> findReposOf(String username) {
        return USER_NAME_TO_REPOS_MAP.get(username);
    }

    public static void clear() {
        USER_NAME_TO_REPOS_MAP.clear();
    }

    public static void renameRepo(String oldRepoName, String newRepoName, String userName) {
        List<Repo> userRepos = findReposOf(userName);
        Repo oldRepo = findRepoBy(oldRepoName, userName);
        Repo renamedRepo = oldRepo.withNewName(newRepoName);
        userRepos.remove(oldRepo);
        userRepos.add(renamedRepo);
        USER_NAME_TO_REPOS_MAP.put(userName, userRepos);
    }

    public static Repo findRepoBy(String repoName, String userName) {
        List<Repo> userRepositories = findReposOf(userName);
        return userRepositories
                .stream()
                .filter(repository -> repository.name().equals(repoName))
                .findFirst().orElseThrow(() -> new RepoNotFoundException("Repository not found"));

    }

    private static Predicate<Repo> withNameOf(Repo newRepo) {
        return repo -> repo.name().equals(newRepo.name());
    }

    private static boolean repoNameAlreadyExists(Repo repo, List<Repo> userRepos) {
        return userRepos.stream()
                .anyMatch(withNameOf(repo));
    }
}