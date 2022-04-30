package xyz.mainframegames.kalabot.commands.coordinate;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.sendErrorMessage;
import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.writeToJson;
import static xyz.mainframegames.kalabot.utils.Names.REMOVE_COMMAND_TITLE;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.javacord.api.entity.channel.ServerTextChannel;
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

public class CoordinateRemove extends AbstractCoordinate {

  private static final Command COMMAND_TYPE = Command.COORDINATE_REMOVE;
  private static final BotError BOT_ERROR = BotError.COORDINATE_REMOVE;
  private static final Pattern pattern =
      Pattern.compile(COMMAND_TYPE.getCommandInput() + " " + Regex.REMOVE_REGEX);

  public CoordinateRemove(MessagingService messagingService) {
    super(COMMAND_TYPE.getCommandInput(), messagingService);
  }

  @Override
  protected synchronized void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {

    if (checkNoCommandSyntaxError(event, BOT_ERROR, channel, COMMAND_TYPE, pattern)) {
      List<String> splitMessage = List.of(event.getMessageContent().split(" "));
      String coordinateName = splitMessage.get(NAME_POSITION);
      MessageAuthor messageAuthor = event.getMessageAuthor();
      AtomicBoolean nameInJson = new AtomicBoolean(false);

      try (FileReader fileReader = new FileReader(JSON_FILE_PATH)) {

        List<CoordinateData> dataList = getJsonCoordinates(fileReader);
        nameInJson.set(checkNameAlreadyInJson(dataList, coordinateName));

        if (nameInJson.get()) {
          removeCoordinateData(jsonList, coordinateName, event);
        } else {
          sendErrorMessage(
              messagingService, messageAuthor, channel, BotError.COORDINATE_DOESNT_EXIST);
        }
      } catch (IOException e) {
        sendErrorMessage(messagingService, messageAuthor, channel, BotError.TECHNICAL_ERROR);
      }
    }
  }

  private void removeCoordinateData(
      List<CoordinateData> jsonList, String coordinateName, MessageCreateEvent event) {
    CoordinateData coordinateDataToRemove;

    Optional<CoordinateData> dataOptional =
        jsonList.stream()
            .filter(coordinateData -> coordinateData.getCoordinateName().equals(coordinateName))
            .findAny();

    if (dataOptional.isPresent()) {
      coordinateDataToRemove = dataOptional.get();
      CoordinateData finalCoordinateDataToRemove = coordinateDataToRemove;
      jsonList.removeIf(coordinateData -> coordinateData.equals(finalCoordinateDataToRemove));

      writeToJson(gson.toJson(jsonList), JSON_FILE_PATH);

      String coordinateX = coordinateDataToRemove.getCoordinateX();
      String coordinateY = coordinateDataToRemove.getCoordinateY();
      String coordinateZ = coordinateDataToRemove.getCoordinateZ();
      String description =
          getCoordinateEmbedDescription(
              coordinateName, coordinateX, coordinateY, coordinateZ, REMOVED);
      messagingService.sendMessageEmbed(
          FunctionsAndPredicates.embedMessageDataBuilder(
              event.getMessageAuthor(),
              REMOVE_COMMAND_TITLE.toString(),
              description,
              null,
              null,
              Color.BLACK),
          event.getChannel());
    }
  }
}
