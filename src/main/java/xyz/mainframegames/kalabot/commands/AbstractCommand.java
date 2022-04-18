package xyz.mainframegames.kalabot.commands;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

public abstract class AbstractCommand implements MessageCreateListener {

  private static final String COMMAND_PREFIX = "!";
  protected final String command;
  protected final MessagingService messagingService;

  protected AbstractCommand(String command, MessagingService messagingService) {
    this.messagingService = messagingService;
    this.command = command;
  }

  @Override
  public void onMessageCreate(MessageCreateEvent event) {

    if(!event.isServerMessage())
      return;

    if(!event.getMessageAuthor().isRegularUser())
      return;

    if(!event.getMessageContent().startsWith(COMMAND_PREFIX+command))
      return;

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

  protected abstract void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user);
}
