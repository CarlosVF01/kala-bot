package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.listeners.AvatarListener;
import xyz.mainframegames.kalabot.services.MessagingService;
import xyz.mainframegames.kalabot.utils.Commands;
import xyz.mainframegames.kalabot.utils.Regex;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AvatarListenerImpl implements AvatarListener {

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private Api api;

    private final static Pattern pattern = Pattern.compile(Commands.AVATAR.toString() + " " + Regex.MENTION_REGEX.toString());

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageContent().startsWith(Commands.AVATAR.toString())){
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());
            if (matcher.matches()){
                try {
                    CompletableFuture<User> user = api.getApi().getUserById(messagingService.formatUserId(matcher.group(1)));
                    messagingService.sendImage(
                            user.get().getAvatar().getUrl().toString() + "?size=1024",
                            messageCreateEvent.getChannel());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                messagingService.sendMessage(
                        messageCreateEvent.getMessageAuthor(),
                        "Avatar command error",
                        "The syntax of the `!avatar` command is: `!avatar [@person]`",
                        null,
                        (String) null,
                        Color.BLACK,
                        messageCreateEvent.getChannel());
            }
        }
    }
}
