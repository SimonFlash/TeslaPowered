package com.mcsimonflash.sponge.teslacore.logger;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

/* TODO:
 * Consider a single LoggerServiceImpl that catches all logged message and outputs
 * them to a unified collection of log files. A useful 'filter' feature might be
 * included to filter out (or in) logs from specific plugins.
 */
/* TODO:
 * Consider implementing the slf4j Logger instead of wrapping an existing one.
 */
/* TODO:
 * If this class implements the slf4j Logger, is it possible to overwrite the
 * Guice @Inject Logger registered by Sponge to return this instead?
 */
/**
 * A logging service that wraps the {@link org.slf4j.Logger}. This logger can
 * save message to an external log file and enable/disable a debugFile mode.
 */
@Deprecated
public class LoggerService {

    private Logger logger;
    private LogFile infoFile, warnFile, errorFile, debugFile;
    private boolean infoMode = true, warnMode = true, errorMode = true, debugMode = true;

    public LoggerService(Logger logger) {
        this.logger = logger;
    }

    public LoggerService(Logger logger, Path logging) {}

    public String getName() {
        return logger.getName();
    }

    public void setInfoMode(boolean enable) {
        infoMode = enable;
    }
    public boolean isInfoEnabled() {
        return infoMode;
    }

    public void setWarnMode(boolean enable) {
        warnMode = enable;
    }
    public boolean isWarnEnabled() {
        return warnMode;
    }

    public void setErrorMode(boolean enable) {
        errorMode = enable;
    }
    public boolean isErrorEnabled() {
        return errorMode;
    }

    public void setDebugMode(boolean enable) {
        debugMode = enable;
    }
    public boolean isDebugEnabled() {
        return debugMode;
    }

    /**
     * Attempts to log a message to the provided {@link LogFile}. If the file
     * for this type of log is not set, this call does nothing.
     * <p>
     * If {@link LogFile#log(String)} throws an {@link IOException}, it will be
     * re-sent as an errorFile. If the file is the {@link #errorFile} file, it will call
     * {@link Logger#error(String, Throwable)}.
     *
     * @param file the file to log the message in
     * @param msg  the message to be logged
     */
    private void logMessage(LogFile file, String msg) {
        if (file != null) {
            try {
                file.log(msg);
            } catch (IOException e) {
                if (file == errorFile) {
                    logger.error("Unable to save log message to file. Message: " + msg, e);
                } else {
                    error("Unable to save log message to file. Message: " + msg, e);
                }
            }
        }
    }

    /**
     * Attempts to log a {@link Throwable}.
     * <p>
     * This constructs a call to {@link #logMessage(LogFile, String)} where the
     * {@param msg} is the stacktrace of the throwable.
     *
     * @param file the file to log the throwable in
     * @param t   the throwable to be logged
     */
    private void logThrowable(LogFile file, Throwable t) {
        if (file != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw, true));
            logMessage(file, sw.getBuffer().toString());
        }
    }

    public void info(String msg) {
        if (infoMode) {
            logMessage(infoFile, msg);
            logger.info(msg);
        }
    }
    public void info(String msg, Throwable t) {
        if (infoMode) {
            logMessage(infoFile, msg);
            logThrowable(infoFile, t);
            logger.info(msg, t);
        }
    }
    public void info(String format, Object arg1) {
        if (infoMode) {
            logMessage(infoFile, String.format(format, arg1));
            logger.info(format, arg1);
        }
    }
    public void info(String format, Object arg1, Object arg2) {
        if (infoMode) {
            logMessage(infoFile, String.format(format, arg1, arg2));
            logger.info(format, arg1, arg2);
        }
    }
    public void info(String format, Object... arguments) {
        if (infoMode) {
            logMessage(infoFile, String.format(format, arguments));
            logger.info(format, arguments);
        }
    }

    public void warn(String msg) {
        if (warnMode) {
            logMessage(warnFile, msg);
            logger.warn(msg);
        }
    }
    public void warn(String msg, Throwable t) {
        if (warnMode) {
            logMessage(warnFile, msg);
            logThrowable(warnFile, t);
            logger.warn(msg, t);
        }
    }
    public void warn(String format, Object arg1) {
        if (warnMode) {
            logMessage(warnFile, String.format(format, arg1));
            logger.warn(format, arg1);
        }
    }
    public void warn(String format, Object arg1, Object arg2) {
        if (warnMode) {
            logMessage(warnFile, String.format(format, arg1, arg2));
            logger.warn(format, arg1, arg2);
        }
    }
    public void warn(String format, Object... arguments) {
        if (warnMode) {
            logMessage(warnFile, String.format(format, arguments));
            logger.warn(format, arguments);
        }
    }

    public void error(String msg) {
        if (errorMode) {
            logMessage(errorFile, msg);
            logger.error(msg);
        }
    }
    public void error(String msg, Throwable t) {
        if (errorMode) {
            logMessage(errorFile, msg);
            logThrowable(errorFile, t);
            logger.error(msg, t);
        }
    }
    public void error(String format, Object arg1) {
        if (errorMode) {
            logMessage(errorFile, String.format(format, arg1));
            logger.error(format, arg1);
        }
    }
    public void error(String format, Object arg1, Object arg2) {
        if (errorMode) {
            logMessage(errorFile, String.format(format, arg1, arg2));
            logger.error(format, arg1, arg2);
        }
    }
    public void error(String format, Object... arguments) {
        if (errorMode) {
            logMessage(errorFile, String.format(format, arguments));
            logger.error(format, arguments);
        }
    }

    public void debug(String msg) {
        if (debugMode) {
            logMessage(debugFile, msg);
            logger.debug(msg);
        }
    }
    public void debug(String msg, Throwable t) {
        if (debugMode) {
            logMessage(debugFile, msg);
            logThrowable(debugFile, t);
            logger.debug(msg, t);
        }
    }
    public void debug(String format, Object arg1) {
        if (debugMode) {
            logMessage(debugFile, String.format(format, arg1));
            logger.debug(format, arg1);
        }
    }
    public void debug(String format, Object arg1, Object arg2) {
        if (debugMode) {
            logMessage(debugFile, String.format(format, arg1, arg2));
            logger.debug(format, arg1, arg2);
        }
    }
    public void debug(String format, Object... arguments) {
        if (debugMode) {
            logMessage(debugFile, String.format(format, arguments));
            logger.debug(format, arguments);
        }
    }

}