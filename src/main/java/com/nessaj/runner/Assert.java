package com.nessaj.runner;

import com.nessaj.runner.exception.MainClassNotFound;

import java.util.Collection;

/**
 * @author keming
 * @Date 2022/04/02 21:47
 */
public class Assert {

    public static void classNotNull(Class<?> clazz, String message) throws MainClassNotFound {
        if (clazz == null) {
            throw new MainClassNotFound(message);
        }
    }

}
