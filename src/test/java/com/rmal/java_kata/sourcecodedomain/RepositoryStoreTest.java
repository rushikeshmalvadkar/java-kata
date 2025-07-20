package com.rmal.java_kata.sourcecodedomain;

import com.rmal.java_kata.sourcecode.Repo;
import com.rmal.java_kata.sourcecode.RepositoryStore;
import com.rmal.java_kata.sourcecode.exceptions.RepoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RepositoryStoreTest {

    @BeforeEach
    void setUp() {
       RepositoryStore.clear();
    }

    @Test
    void should_create_one_repo() {
        Repo repo1 = Repo.of("java");
        String  repoName = RepositoryStore.add("rushikesh", repo1);
        List<Repo> repoList = RepositoryStore.findAll("rushikesh");
        assertThat(repoName).isEqualTo("java");
        assertThat(repoList).hasSize(1);
    }

    @Test
    void should_create_two_repo() {
        Repo repo1 = Repo.of("java");
        String  repoName = RepositoryStore.add("rushikesh", repo1);

        Repo repo2 = Repo.of("spring-kata");
        String  repoName2 = RepositoryStore.add("rushikesh", repo2);

        List<Repo> repoList = RepositoryStore.findAll("rushikesh");
        assertThat(repoList).hasSize(2);
    }

    @Test
    @DisplayName("Will throw exception")
    void should_when_create_duplicate_repo(){
        Repo repo1 = Repo.of("java");
        Repo repo2 = Repo.of("java");
        RepositoryStore.add("rushikesh",repo1);
        assertThatThrownBy(() -> RepositoryStore.add("rushikesh", repo2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("repository already exist");

    }

    @Test
    void should_rename_existing_repository_of_user(){
        Repo repo1 = Repo.of("java");
        String  repoName = RepositoryStore.add("rushikesh", repo1);
       RepositoryStore.renameRepo("java", "java-kata","rushikesh");
        Repo renameRepo = RepositoryStore.findRepoBy("java-kata", "rushikesh");
        assertThat(renameRepo.getName()).isEqualTo("java-kata");
        assertThatThrownBy(()-> RepositoryStore.findRepoBy("java", "rushikesh"))
                .isInstanceOf(RepoNotFoundException.class).hasMessage("repoNotFound");
    }

    @Test
    void should_try_to_rename_not_exist_repository_of_user(){
        Repo repo1 = Repo.of("java");
        String  repoName = RepositoryStore.add("rushikesh", repo1);
        assertThatThrownBy(()-> RepositoryStore.renameRepo("ja", "java-kata","rushikesh"))
                .isInstanceOf(RepoNotFoundException.class).hasMessage("Repository not found: ja");
    }

    @Test
    void should_delete_delete_repo(){
        Repo repo1 = Repo.of("java");
        String  repoName = RepositoryStore.add("rushikesh", repo1);
        RepositoryStore.deleteRepo("java","rushikesh");
        assertThatThrownBy(()-> RepositoryStore.findRepoBy("java", "rushikesh"))
                .isInstanceOf(RepoNotFoundException.class).hasMessage("repoNotFound");
    }


}
