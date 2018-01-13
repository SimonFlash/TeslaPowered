package com.mcsimonflash.sponge.teslacore.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * IO is not my expertise. Review of this to verify it's functionality and make
 * any (probably much needed) improvements is greatly appreciated.
 */
/**
 * A file for logging message.
 */
@Deprecated
public class LogFile {

    private final BufferedWriter writer;

    /**
     * Creates a new instance for the given path.
     *
     * @param path the path to the file
     * @throws IOException if the file could not be deleted, created, or written
     */
    public LogFile(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path);
    }

    /**
     * Logs a message to the file.
     *
     * @param msg the message to be logged
     * @throws IOException if the message could not be logged
     */
    public void log(String msg) throws IOException {
        try {
            writer.write(msg);
            writer.newLine();
        } catch (IOException e) {
            writer.close();
            throw e;
        }
    }

}