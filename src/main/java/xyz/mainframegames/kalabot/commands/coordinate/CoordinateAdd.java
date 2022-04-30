package xyz.mainframegames.kalabot.commands.coordinate;

import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.sendErrorMessage;
import static xyz.mainframegames.kalabot.utils.FunctionsAndPredicates.writeToJson;
import static xyz.mainframegames.kalabot.utils.Names.ADD_COMMAND_TITLE;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class CoordinateAdd extends AbstractCoordinate {

  private static final Command COMMAND_TYPE = Command.COORDINATE_ADD;
  private static final BotError BOT_ERROR = BotError.COORDINATE_ADD;
  private static final Pattern pattern =
      Pattern.compile(COMMAND_TYPE.getCommandInput() + " " + Regex.ADD_REGEX);

  public CoordinateAdd(MessagingService messagingService) {
    super(COMMAND_TYPE.getCommandInput(), messagingService);
  }

  @Override
  protected void runCommand(
      MessageCreateEvent event, Server server, ServerTextChannel channel, User user) {

    if (checkNoCommandSyntaxError(event, BOT_ERROR, channel, COMMAND_TYPE, pattern)) {
      List<String> splitMessage = List.of(event.getMessageContent().split(" "));
      String coordinateName = splitMessage.get(NAME_POSITION);
      MessageAuthor messageAuthor = event.getMessageAuthor();
      AtomicBoolean nameAlreadyInJson = new AtomicBoolean(false);
      AtomicBoolean coordinatesAlreadyInJson = new AtomicBoolean(false);

      try (FileReader fileReader = new FileReader(JSON_FILE_PATH)) {
        List<CoordinateData> dataList = getJsonCoordinates(fileReader);

        String xCoordinate = splitMessage.get(X_POSITION);
        String yCoordinate = splitMessage.get(Y_POSITION);
        String zCoordinate = splitMessage.get(Z_POSITION);

        coordinatesAlreadyInJson.set(
            checkPositionsAlreadyInJson(dataList, xCoordinate, yCoordinate, zCoordinate));
        nameAlreadyInJson.set(checkNameAlreadyInJson(dataList, coordinateName));
        if (nameAlreadyInJson.get() || coordinatesAlreadyInJson.get()) {
          sendErrorMessage(
              messagingService, messageAuthor, channel, BotError.COORDINATE_ALREADY_EXISTS);
        } else {
          writeCoordinateData(
              xCoordinate, yCoordinate, zCoordinate, coordinateName, jsonList, event);
        }
      } catch (IOException e) {
        sendErrorMessage(messagingService, messageAuthor, channel, BotError.TECHNICAL_ERROR);
      }
    }
  }

  private void writeCoordinateData(
      String xCoordinate,
      String yCoordinate,
      String zCoordinate,
      String coordinateName,
      List<CoordinateData> jsonData,
      MessageCreateEvent event) {
    String dateOfAddition = LocalDate.now().toString();

    CoordinateData coordinateData =
        CoordinateData.builder()
            .coordinateName(coordinateName)
            .coordinateX(xCoordinate)
            .coordinateY(yCoordinate)
            .coordinateZ(zCoordinate)
            .dateOfAddition(dateOfAddition)
            .build();

    jsonData.add(coordinateData);

    writeToJson(gson.toJson(jsonData), JSON_FILE_PATH);

    String description =
        getCoordinateEmbedDescription(
            coordinateName,
            coordinateData.getCoordinateX(),
            coordinateData.getCoordinateY(),
            coordinateData.getCoordinateZ(),
            ADDED);

    messagingService.sendMessageEmbed(
        FunctionsAndPredicates.embedMessageDataBuilder(
            event.getMessageAuthor(),
            ADD_COMMAND_TITLE.toString(),
            description,
            null,
            null,
            Color.BLACK),
        event.getChannel());
  }
}
