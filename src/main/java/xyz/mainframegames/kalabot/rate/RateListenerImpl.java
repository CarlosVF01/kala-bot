package xyz.mainframegames.kalabot.rate;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.enums.Commands;
import xyz.mainframegames.kalabot.utils.enums.Errors;
import xyz.mainframegames.kalabot.utils.enums.Regex;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.mainframegames.kalabot.utils.enums.Names.BOT_NAME;

@Slf4j
@Component
public class RateListenerImpl implements RateListener {

    @Autowired
    private Api api;
    @Autowired
    private MessagingService messagingService;

    //Pattern required for the command to work
    private static final Pattern pattern = Pattern.compile(Commands.RATE + " " + Regex.MENTION_REGEX);


    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().startsWith(Commands.RATE.toString())) {
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());
            //Checks if the user wrote the correct regex
            if (matcher.matches()) {
                //Generates the random number out of 10
                int rating = new Random().nextInt(10);

                try {
                    messagingService.sendMessageEmbedWithReactionListenerDelete(
                            messageCreateEvent.getMessageAuthor(),
                            "Rate calculator",
                            matcher.group(1) + " is a " + rating + "/10",
                            null,
                            api.getDiscordApi().getUserById(messagingService.formatUserId(matcher.group(1))).get().getAvatar(),
                            Color.BLACK,
                            messageCreateEvent);

                } catch (InterruptedException | ExecutionException e) {
                    log.error("Rate Error {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            } else if (!messageCreateEvent.getMessageAuthor().getName().equals(BOT_NAME.toString())) {
                messagingService.sendMessageEmbed(
                        messageCreateEvent.getMessageAuthor(),
                        Errors.RATE.getError(),
                        Errors.RATE.getDescription(),
                        null,
                        null,
                        Color.BLACK,
                        messageCreateEvent.getChannel()
                );
            }
        }
    }
}