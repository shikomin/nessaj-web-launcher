package com.nessaj.runner;

import com.nessaj.runner.exception.MainClassNotFound;

import java.lang.reflect.InvocationTargetException;

/**
 * @author keming
 * @Date 2022/04/02 19:23
 */
public interface Runner {

    void run() throws MainClassNotFound, InvocationTargetException, IllegalAccessException;

}
