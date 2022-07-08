package xyz.mainframegames.kalabot.commands;

import static xyz.mainframegames.kalabot.utils.Names.COORDINATES_LOCAL_FILE;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import xyz.mainframegames.kalabot.commands.AbstractCommand;
import xyz.mainframegames.kalabot.data.CoordinateData;
import xyz.mainframegames.kalabot.services.messages.MessagingService;

public abstract class AbstractCoordinate extends AbstractCommand {

  protected static final String REMOVED = " has been removed";
  protected static final String ADDED = " has been added";
  protected static final int NAME_POSITION = 1;
  protected static final int X_POSITION = 2;
  protected static final int Y_POSITION = 3;
  protected static final int Z_POSITION = 4;
  protected static final Gson gson = new Gson();
  protected static final String JSON_FILE_PATH = COORDINATES_LOCAL_FILE.toString();

  protected List<CoordinateData> jsonList = new ArrayList<>();

  protected AbstractCoordinate(String command, MessagingService messagingService) {
    super(command, messagingService);
  }

  protected boolean checkNameAlreadyInJson(
      List<CoordinateData> coordinateDataList, String coordinateName) {
    jsonList = coordinateDataList;
    return coordinateDataList.stream()
        .map(CoordinateData::getCoordinateName)
        .anyMatch(name -> name.equals(coordinateName));
  }

  protected boolean checkPositionsAlreadyInJson(
      List<CoordinateData> coordinateDataList,
      String xCoordinate,
      String yCoordinate,
      String zCoordinate) {
    jsonList = coordinateDataList;
    return coordinateDataList.stream()
        .anyMatch(
            coordinateData ->
                coordinateData.getCoordinateX().equals(xCoordinate)
                    && coordinateData.getCoordinateY().equals(yCoordinate)
                    && coordinateData.getCoordinateZ().equals(zCoordinate));
  }

  protected String getCoordinateEmbedDescription(
      String coordinateName,
      String coordinateX,
      String coordinateY,
      String coordinateZ,
      String finalWord) {
    return "The coordinate `"
        + coordinateName
        + "` with the positions:\nX: "
        + coordinateX
        + "\nY: "
        + coordinateY
        + "\nZ: "
        + coordinateZ
        + "\n"
        + finalWord;
  }

  protected List<CoordinateData> getJsonCoordinates(Reader fileReader) {
    return new Gson().fromJson(fileReader, new TypeToken<List<CoordinateData>>() {}.getType());
  }
}
