package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.listeners.TestListener;

@Component
public class TestListenerImpl implements TestListener {

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().equals("!test")){
            messageCreateEvent.getChannel().sendMessage("You're currently testing");
        }
    }
}
