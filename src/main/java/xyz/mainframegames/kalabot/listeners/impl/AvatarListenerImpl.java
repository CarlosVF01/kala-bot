package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.listeners.AvatarListener;
import xyz.mainframegames.kalabot.services.MessagingService;
import xyz.mainframegames.kalabot.utils.Commands;
import xyz.mainframegames.kalabot.utils.Errors;
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

    private final static Pattern pattern = Pattern.compile(Commands.AVATAR + " " + Regex.MENTION_REGEX);

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageContent().startsWith(Commands.AVATAR.toString())){

            TextChannel channel = messageCreateEvent.getChannel();
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());

            if (matcher.matches()){
                try {
                    CompletableFuture<User> user = api.getApi().getUserById(messagingService.formatUserId(matcher.group(1)));
                    messagingService.sendImage(
                            user.get().getAvatar().getUrl().toString() + "?size=1024",
                            channel);
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error ocurred in AVATAR commamnd " + e);
                }
            } else {
                messagingService.sendMessageEmbed(
                        messageCreateEvent.getMessageAuthor(),
                        Errors.AVATAR.getError(),
                        Errors.AVATAR.getDescription(),
                        null,
                        (String) null,
                        Color.BLACK,
                        channel);
            }
        }
    }
}
