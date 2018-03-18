package com.mcsimonflash.sponge.teslacore.tesla;

import com.mcsimonflash.sponge.teslalibs.message.MessageService;
import org.spongepowered.api.asset.Asset;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

public class TeslaUtils {

    public static Optional<URL> parseURL(String url) {
        try {
            return Optional.of(new URL(url));
        } catch (MalformedURLException ignored) {
            return Optional.empty();
        }
    }

    public static MessageService getMessageService(Tesla tesla) {
        Asset messages = tesla.getContainer().getAsset("messages.properties").orElse(null);
        if (messages != null) {
            Path translations = tesla.getDirectory().resolve("translations");
            try {
                messages.copyToDirectory(translations);
                return MessageService.of(translations, "messages");
            } catch (IOException e) {
                tesla.getLogger().error("An error occurred initializing message translations. Using internal copies.");
            }
        }
        return MessageService.of(tesla.getContainer(), "messages");
    }

}