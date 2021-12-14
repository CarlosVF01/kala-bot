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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.mainframegames.kalabot.utils.enums.FunctionsAndPredicates.authorIsNotBot;
import static xyz.mainframegames.kalabot.utils.enums.FunctionsAndPredicates.sendErrorMessage;

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

            TextChannel textChannel = messageCreateEvent.getChannel();
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());

            if (matcher.matches()) {
                CompletableFuture<User> user = api.getDiscordApi().getUserById(messagingService.formatUserId(matcher.group(1)));
                try {
                    String userAvatarUrl = user.get().getAvatar().getUrl().toString();
                    messagingService.sendImage(userAvatarUrl + "?size=1024", textChannel);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Avatar error {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            } else if (authorIsNotBot.test(messageCreateEvent)) {
                sendErrorMessage(messagingService, messageCreateEvent.getMessageAuthor(), textChannel, Errors.AVATAR);
            }
        }
    }
}
