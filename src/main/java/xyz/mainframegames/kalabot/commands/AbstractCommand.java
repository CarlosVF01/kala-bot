package xyz.mainframegames.kalabot.commands;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.sendErrorMessage;

import java.util.regex.Pattern;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;

/**
 * Class to reduce duplicity of code when creating commands
 */
public abstract class AbstractCommand implements MessageCreateListener {

  protected final String command;
  protected final MessagingService messagingService;

  protected AbstractCommand(String command, MessagingService messagingService) {
    this.messagingService = messagingService;
    this.command = command;
  }

  @Override
  public void onMessageCreate(MessageCreateEvent event) {

    if (!event.isServerMessage()) {
      return;
    }

    if (!event.getMessageAuthor().isRegularUser()) {
      return;
    }

    if (!event.getMessageContent().split(" ")[0].equals(command)) {
      return;
    }

    event
        .getServer()
        .ifPresent(
            server ->
                event
                    .getMessageAuthor()
                    .asUser()
                    .ifPresent(
                        user ->
                            event
                                .getServerTextChannel()
                                .ifPresent(
                                    serverTextChannel ->
                                        runCommand(event, server, serverTextChannel, user))));
  }

  /**
   * Checks if there's a syntax error in the command introduced by the user
   *
   * @param event    message event {@link MessageCreateEvent}
   * @param botError error depending on the command {@link BotError}
   * @param channel  event channel {@link ServerTextChannel}
   * @param command  command introduced {@link Command}
   * @param pattern  pattern used to check the user input {@link Pattern} if it's null there won't
   *                 be a check
   * @return if there's an error (true) or not (false)
   */
  protected boolean checkNoCommandSyntaxError(
      MessageCreateEvent event, BotError botError, ServerTextChannel channel, Command command,
      Pattern pattern) {
    String messageContent = event.getMessageContent();
    boolean messageStartsWithCommand = messageContent.startsWith(command.toString());
    if (pattern != null) {
      if (messageStartsWithCommand
          && pattern.matcher(messageContent).matches()) {
        return true;
      }
    } else {
      if (messageStartsWithCommand) {
        return true;
      }
    }
    sendErrorMessage(messagingService, event.getMessageAuthor(), channel, botError);
    return false;
  }

  protected abstract void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user);
}
