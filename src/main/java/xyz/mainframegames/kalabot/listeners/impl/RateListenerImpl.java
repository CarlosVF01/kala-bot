package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.listeners.RateListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RateListenerImpl implements RateListener {
    private final static String COMMAND_WORD = "!rate";
    //Pattern required for the command to work
    private final static Pattern pattern = Pattern.compile(COMMAND_WORD + " (\\w+)");

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().startsWith(COMMAND_WORD)){
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());
            if (matcher.matches()){
                int rating = (int) Math.floor(Math.random() * 10) + 1;
                messageCreateEvent
                        .getChannel()
                        .sendMessage(
                                matcher.group(1) + " is a " + rating + "/10"
                        );
            } else {
                messageCreateEvent
                        .getChannel()
                        .sendMessage("The syntax of the `!rate` command is: `!rate [word]`");
            }
        }
    }
}