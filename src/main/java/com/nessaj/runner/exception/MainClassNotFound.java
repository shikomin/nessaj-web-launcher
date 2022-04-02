package com.nessaj.runner.exception;

/**
 * @author keming
 * @Date 2022/04/02 21:30
 */
public class MainClassNotFound extends Exception {

    String msg = "can not find the main class in this path: ";

    public MainClassNotFound() {
    }

    public MainClassNotFound(String msg) {
        this.msg = msg;
    }

}
