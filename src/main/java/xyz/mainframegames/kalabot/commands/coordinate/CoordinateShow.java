package xyz.mainframegames.kalabot.commands.coordinate;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.sendErrorMessage;
import static xyz.mainframegames.kalabot.utils.Names.SHOW_COMMAND_TITLE;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import xyz.mainframegames.kalabot.commands.AbstractCoordinate;
import xyz.mainframegames.kalabot.data.CoordinateData;
import xyz.mainframegames.kalabot.services.messages.MessagingService;
import xyz.mainframegames.kalabot.utils.BotError;
import xyz.mainframegames.kalabot.utils.Command;
import xyz.mainframegames.kalabot.utils.FunctionsAndPredicates;
import xyz.mainframegames.kalabot.utils.Regex;

@Slf4j
public class CoordinateShow extends AbstractCoordinate {

  private static final Command COMMAND_TYPE = Command.COORDINATE_SHOW_ALL;
  private static final BotError BOT_ERROR = BotError.COORDINATE_SHOW_ALL;
  private static final Pattern pattern =
      Pattern.compile(COMMAND_TYPE.getCommandInput() + " " + Regex.ANY_NUMBER_REGEX);

  private static final int PAGE_SIZE = 20;

  public CoordinateShow(MessagingService messagingService) {
    super(COMMAND_TYPE.getCommandInput(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {
    if (checkNoCommandSyntaxError(event, BOT_ERROR, channel, COMMAND_TYPE, pattern)) {
      MessageAuthor messageAuthor = event.getMessageAuthor();
      try (FileReader fileReader = new FileReader(JSON_FILE_PATH)) {
        List<CoordinateData> dataList = getJsonCoordinates(fileReader);

        if (!dataList.isEmpty()) {
          jsonList = dataList;
          sendCoordinateMessage(event);
        } else {
          sendErrorMessage(messagingService, messageAuthor, channel, BotError.NO_COORDINATES_ADDED);
        }

      } catch (IOException e) {
        sendErrorMessage(messagingService, messageAuthor, channel, BotError.TECHNICAL_ERROR);
      }
    }
  }

  private void sendCoordinateMessage(MessageCreateEvent event) {
    MessageAuthor messageAuthor = event.getMessageAuthor();
    TextChannel channel = event.getChannel();
    int numberOfPages = jsonList.size() < PAGE_SIZE ? 1 : jsonList.size() / PAGE_SIZE + 1;

    try {
      int pageNumber = Integer.parseInt(event.getMessageContent().split(" ")[1]);
      int lastIndex =
          jsonList.size() <= PAGE_SIZE
              ? jsonList.size()
              : (Math.min(PAGE_SIZE * pageNumber, jsonList.size()));
      int firstIndex = jsonList.size() <= PAGE_SIZE ? 0 : PAGE_SIZE * (pageNumber -1);

      List<CoordinateData> pageData = jsonList.subList(firstIndex, lastIndex);
      messagingService.sendMessageEmbed(
          FunctionsAndPredicates.embedMessageDataBuilder(
              messageAuthor,
              SHOW_COMMAND_TITLE.toString(),
              formatDescription(pageData),
              "Page " + pageNumber + "/" + numberOfPages,
              null,
              Color.BLACK),
          channel);
    } catch (IndexOutOfBoundsException e) {
      sendErrorMessage(
          messagingService, messageAuthor, channel, BotError.COORDINATE_PAGE_DOESNT_EXIST);
      channel.sendMessage(
          "There's a total of `"
              + jsonList.size()
              + "` coordinates, making it a total of `"
              + numberOfPages
              + "` pages");
    }
  }

  private String formatDescription(List<CoordinateData> pageData) {
    return pageData.stream()
        .map(
            coordinateData ->
                "`"
                    + coordinateData.getCoordinateName()
                    + "`: `X:"
                    + coordinateData.getCoordinateX()
                    + "` `Y:"
                    + coordinateData.getCoordinateY()
                    + "` `Z:"
                    + coordinateData.getCoordinateZ()
                    + "` `("
                    + coordinateData.getDateOfAddition()
                    + ")`")
        .collect(Collectors.joining(NEW_LINE + NEW_LINE));
  }
}
