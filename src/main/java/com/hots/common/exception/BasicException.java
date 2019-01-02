package com.hots.common.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;

public class BasicException extends Exception {

    private static final long serialVersionUID = 1L;

    static {
        URL url = BasicException.class.getClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(url.getPath());
    }

    private static final Logger logger = LogManager.getLogger(BasicException.class);

    public BasicException(String message, Object obj, Throwable cause) {
        logger.error("---------------------Begin------------------------");
        if (obj != null) {
            logger.error(message + "(" + obj.toString() + ")");
        } else {
            logger.error(message);
        }

        if (cause != null) {
            StackTraceElement[] stackElements = cause.getStackTrace();
            if (stackElements != null && stackElements.length > 0) {
                for (int i = 0; i < stackElements.length; i++) {
                    logger.error(stackElements[i].getClassName() + "[" + stackElements[i].getMethodName() + ":" + stackElements[i].getLineNumber() + "]");
                }
            }

            cause.printStackTrace();
        }
        logger.error("---------------------The end------------------------");
    }

}
