package com.rmal.java_kata.sourcecode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public  class Repo {
    private  String name;


    public Repo(String name) {
        this.name = name;
    }

    public static Repo of(String name) {
        return new Repo(name);
    }

}
