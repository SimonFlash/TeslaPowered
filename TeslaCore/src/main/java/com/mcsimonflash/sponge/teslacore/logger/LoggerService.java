package com.mcsimonflash.sponge.teslacore.logger;

import com.mcsimonflash.sponge.teslalibs.message.Message;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class LoggerService {

    //TODO: System to display logged messages to players online
    //TODO: Allow messages to be filtered
    //TODO: Save messages to a specific log file

    private final Text INFO, WARN, ERROR, DEBUG;

    private LoggerService(PluginContainer container) {
        INFO = Text.of(container.getId(), "/INFO: ", TextColors.GREEN);
        WARN = Text.of(container.getId(), "/WARN: ", TextColors.YELLOW);
        ERROR = Text.of(container.getId(), "/ERROR: ", TextColors.RED);
        DEBUG = Text.of(container.getId(), "/DEBUG: ", TextColors.LIGHT_PURPLE);
    }

    public static LoggerService of(PluginContainer container) {
        return new LoggerService(container);
    }

    private void log(Text level, Text msg) {
        Sponge.getServer().getConsole().sendMessage(level.concat(msg));
    }

    private void log(Text level, String msg) {
        log(level, Message.of(msg).toText());
    }

    private void log(Text level, String msg, Object... args) {
        log(level, Message.of(msg).args(args).toText());
    }

    public void info(Text msg) {
        log(INFO, msg);
    }

    public void info(String msg) {
        log(INFO, msg);
    }

    public void info(String template, Object... args) {
        log(INFO, template, args);
    }

    public void warn(Text msg) {
        log(WARN, msg);
    }

    public void warn(String msg) {
        log(WARN, msg);
    }

    public void warn(String template, Object... args) {
        log(WARN, template, args);
    }

    public void error(Text msg) {
        log(ERROR, msg);
    }

    public void error(String msg) {
        log(ERROR, msg);
    }

    public void error(String template, Object... args) {
        log(ERROR, template, args);
    }

    public void debug(Text msg) {
        log(DEBUG, msg);
    }

    public void debug(String msg) {
        log(DEBUG, msg);
    }

    public void debug(String template, Object... args) {
        log(DEBUG, template, args);
    }

}