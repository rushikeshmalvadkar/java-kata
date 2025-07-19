package com.rmal.java_kata.sourcecode;

public record Repo( String name  ) {

    public Repo(String name ){
        this .name= name;
    }

    public static Repo of(String name){
        return new Repo(name);
    }
}
