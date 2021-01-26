package de.carldressler.autovoice.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(String classname) {
        return LoggerFactory.getLogger(classname);
    }
}
