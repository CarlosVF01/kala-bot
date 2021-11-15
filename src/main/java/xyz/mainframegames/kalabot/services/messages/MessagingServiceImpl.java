package xyz.mainframegames.kalabot.services.messages;

import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static xyz.mainframegames.kalabot.utils.enums.Names.BOT_NAME;

@Service
@Slf4j
public class MessagingServiceImpl implements MessagingService {

    @Override
    public void sendMessageEmbedWithReactionListenerDelete(MessageAuthor author, String title, String description, String footer, Icon thumbnail, Color color, MessageCreateEvent event) {
        //Creates a reaction listener in the channel
        event.getChannel().addReactionAddListener(reactionAddEvent -> {
            Optional<Message> message = reactionAddEvent.getMessage();
            if (message.isPresent()) {
                if (reactionAddEvent.getEmoji().equalsEmoji("👎") && message.get().getAuthor().getName().equals(BOT_NAME.toString())) {
                    //The unicode is of the :pensive: emote
                    event.getChannel().sendMessage("Sorry you didn't like it \uD83D\uDE14");
                    reactionAddEvent.deleteMessage();
                }
            } else {
                log.error("Can this no message present error even happen?");
            }
        }).removeAfter(30, TimeUnit.SECONDS);

        new MessageBuilder().setEmbed(new EmbedBuilder()
                .setAuthor(author)
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setThumbnail(thumbnail)
                .setColor(color)
        ).send(event.getChannel());
    }

    public void sendMessageEmbed(MessageAuthor author, String title, String description, String footer, String thumbnail, Color color, TextChannel channel) {
        new MessageBuilder().setEmbed(new EmbedBuilder()
                        .setAuthor(author)
                        .setTitle(title)
                        .setDescription(description)
                        .setFooter(footer)
                        .setThumbnail(thumbnail)
                        .setColor(color))
                .send(channel);
    }

    public void sendImage(Icon image, TextChannel channel) {
        new MessageBuilder().setEmbed(new EmbedBuilder()
                .setImage(image)
        ).send(channel);
    }

    public void sendImage(String image, TextChannel channel) {
        new MessageBuilder().setEmbed(new EmbedBuilder()
                .setImage(image)
        ).send(channel);
    }

    //Method to format the user IDs that the bot gets from a mention
    //Formatted User ID example: 123456789123456789
    //All of them have 18 numbers that represent the ID
    //But when doing a mention the ID given can look like this based on if their nickname is changed or not:
    //Example 1: <@!123456789123456789>
    //Example 2: <@123456789123456789>
    //So to use the User ID first it needs to be formatted based on what example is the case
    public long formatUserId(String word) {
        //If the user id has a ! the substring is different
        return Long.parseLong(word.substring(!Character.isDigit(word.charAt(2)) ? 3 : 2, word.length() - 1));
    }


}
