package xyz.mainframegames.kalabot.commands.help;

import static xyz.mainframegames.kalabot.utils.Names.HELP_COMMAND_TITLE;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.Command;

public class HelpCommand extends AbstractCommand {

  public HelpCommand(MessagingService messagingService) {
    super(Command.HELP.toString(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    Command[] values = Command.values();

    String commandDescription =
        Stream.of(values)
            .map(command -> command.getCommandInput() + ": " + command.getCommandDescription())
            .collect(Collectors.joining("\n"));
    messagingService.sendMessageEmbed(
        event.getMessageAuthor(),
        HELP_COMMAND_TITLE.toString(),
        commandDescription,
        null,
        null,
        null,
        channel);
  }
}
