package com.matchadb.instrumentation;

import java.lang.instrument.Instrumentation;

/**
 * A utility that loads up before the application starts so that we can return
 * metrics of the application to required services, such as determining DB size.
 */
public class MatchaDbInstrumentationTool {
    private static volatile Instrumentation instrumentation;

    public static void premain(final String args, final Instrumentation instrum) {
        instrumentation = instrum;
    }

    public static long getObjectSize(Object object) {
        return instrumentation.getObjectSize(object);
    }
}