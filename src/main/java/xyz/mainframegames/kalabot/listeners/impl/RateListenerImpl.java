package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.listeners.RateListener;
import xyz.mainframegames.kalabot.services.MessagingService;
import xyz.mainframegames.kalabot.utils.Commands;
import xyz.mainframegames.kalabot.utils.Errors;
import xyz.mainframegames.kalabot.utils.Regex;

import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RateListenerImpl implements RateListener {

    @Autowired
    private Api api;
    @Autowired
    private MessagingService messagingService;

    //Pattern required for the command to work
    private final static Pattern pattern = Pattern.compile(Commands.RATE + " " + Regex.MENTION_REGEX);

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().startsWith(Commands.RATE.toString())){
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());
            //Checks if the user wrote the correct regex
            if (matcher.matches()){
                //Generates the random number out of 10
                int rating = (int) Math.floor(Math.random() * 10)+ 1;
                try {
                    messagingService.sendMessageEmbed(
                            messageCreateEvent.getMessageAuthor(),
                            "Rate calculator",
                            matcher.group(1) + " is a " + rating + "/10",
                            null,
                            api.getApi().getUserById(messagingService.formatUserId(matcher.group(1))).get().getAvatar(),
                            Color.BLACK,
                            messageCreateEvent.getChannel());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error ocurred in RATE command " + e);
                }
            } else {
               messagingService.sendMessageEmbed(
                       messageCreateEvent.getMessageAuthor(),
                       Errors.RATE.getError(),
                       Errors.RATE.getDescription(),
                       null,
                       (String) null,
                       Color.BLACK,
                       messageCreateEvent.getChannel()
               );
            }
        }
    }
}