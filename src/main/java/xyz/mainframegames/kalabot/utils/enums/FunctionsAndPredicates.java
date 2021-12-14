package xyz.mainframegames.kalabot.utils.enums;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

import java.awt.*;

import java.util.function.Predicate;

import static xyz.mainframegames.kalabot.utils.enums.Names.BOT_NAME;

public class FunctionsAndPredicates {

    public static Predicate<MessageCreateEvent> authorIsNotBot = condition -> !condition.getMessageAuthor().getName().equals(BOT_NAME.toString());

    public static void sendErrorMessage(MessagingService messagingService, MessageAuthor messageAuthor, TextChannel textChannel, Errors errors) {
        messagingService.sendMessageEmbed(
                messageAuthor,
                errors.getError(),
                errors.getDescription(),
                null,
                null,
                Color.BLACK,
                textChannel);
    }
}
