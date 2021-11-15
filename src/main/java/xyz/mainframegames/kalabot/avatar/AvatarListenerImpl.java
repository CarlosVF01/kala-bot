package xyz.mainframegames.kalabot.avatar;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.enums.Commands;
import xyz.mainframegames.kalabot.utils.enums.Errors;
import xyz.mainframegames.kalabot.utils.enums.Regex;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.mainframegames.kalabot.utils.enums.Names.BOT_NAME;

@Component
@Slf4j
public class AvatarListenerImpl implements AvatarListener {

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private Api api;

    private static final Pattern pattern = Pattern.compile(Commands.AVATAR + " " + Regex.MENTION_REGEX);


    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().startsWith(Commands.AVATAR.toString())) {

            TextChannel channel = messageCreateEvent.getChannel();
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());

            if (matcher.matches()) {
                CompletableFuture<User> user = api.getDiscordApi().getUserById(messagingService.formatUserId(matcher.group(1)));
                try {
                    messagingService.sendImage(
                            user.get()
                                    .getAvatar()
                                    .getUrl()
                                    .toString() + "?size=1024", channel);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Avatar error {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            } else if(!messageCreateEvent.getMessageAuthor().getName().equals(BOT_NAME.toString())) {
                messagingService.sendMessageEmbed(
                        messageCreateEvent.getMessageAuthor(),
                        Errors.AVATAR.getError(),
                        Errors.AVATAR.getDescription(),
                        null,
                        null,
                        Color.BLACK,
                        channel);
            }
        }
    }
}
