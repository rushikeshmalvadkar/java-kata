package com.rmal.java_kata.sourcecode.exceptions;

public class RepoNotFoundException extends RuntimeException{

    public RepoNotFoundException(String errorMessage){
      super(errorMessage);
    }
}
