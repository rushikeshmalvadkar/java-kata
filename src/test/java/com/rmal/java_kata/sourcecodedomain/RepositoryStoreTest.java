package com.rmal.java_kata.sourcecodedomain;

import com.rmal.java_kata.sourcecode.Repo;
import com.rmal.java_kata.sourcecode.RepositoryStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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



}
