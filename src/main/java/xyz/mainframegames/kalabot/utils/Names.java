package xyz.mainframegames.kalabot.utils;

/** Enum with names used throughout the whole application */
public enum Names {
  BOT_NAME("Kala Bot"),
  RATE_COMMAND_TITLE("Rate calculator"),
  HELP_COMMAND_TITLE("List of commands"),
  REMOVE_COMMAND_TITLE("Coordinate removed"),
  ADD_COMMAND_TITLE("Coordinate added"),
  SHOW_COMMAND_TITLE("Coordinates list"),
  COORDINATES_LOCAL_FILE("src/main/resources/local/coordinates.json");

  private final String text;

  Names(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
