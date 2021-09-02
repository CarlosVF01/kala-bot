package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.Api;
import xyz.mainframegames.kalabot.listeners.RateListener;
import xyz.mainframegames.kalabot.services.MessagingService;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RateListenerImpl implements RateListener {

    @Autowired
    private Api api;
    @Autowired
    private MessagingService messagingService;

    private final static int[] DEFAULT_COLOR = new int[]{0,0,0};
    private final static String COMMAND_WORD = "!rate";
    //Pattern required for the command to work
    private final static Pattern pattern = Pattern.compile(COMMAND_WORD + " (<@.?[0-9]*?>)");

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().startsWith(COMMAND_WORD)){
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());
            //Checks if the user wrote the correct regex
            if (matcher.matches()){
                //Generates the random number out of 10
                int rating = (int) Math.floor(Math.random() * 10)+ 1;
                try {
                    messagingService.sendMessage(
                            messageCreateEvent.getMessageAuthor(),
                            "Rate calculator",
                            matcher.group(1) + " is a " + rating + "/10",
                            null,
                            api.getApi().getUserById(messagingService.formatUserId(matcher.group(1))).get().getAvatar(),
                            DEFAULT_COLOR,
                            messageCreateEvent.getChannel());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
               messagingService.sendMessage(
                       messageCreateEvent.getMessageAuthor(),
                       "Rate calculator",
                       "The syntax of the `!rate` command is: `!rate [@person]`",
                       null,
                       (String) null,
                       DEFAULT_COLOR,
                       messageCreateEvent.getChannel());
            }
        }
    }
}