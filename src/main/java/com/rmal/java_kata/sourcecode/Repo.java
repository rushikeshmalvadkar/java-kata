package com.rmal.java_kata.sourcecode;

import lombok.Value;


public record Repo(String name) {

    public static Repo of(String name) {
        return new Repo(name);
    }

    public Repo withNewName(String newRepoName) {
        return new Repo(newRepoName);
    }
}
