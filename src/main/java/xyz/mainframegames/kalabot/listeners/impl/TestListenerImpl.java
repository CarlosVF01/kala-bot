package xyz.mainframegames.kalabot.listeners.impl;

import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import xyz.mainframegames.kalabot.listeners.TestListener;
import xyz.mainframegames.kalabot.utils.Commands;

@Component
public class TestListenerImpl implements TestListener {

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().equals(Commands.TEST.toString())){
            messageCreateEvent.getChannel().sendMessage("You're currently testing");
        }
    }
}
