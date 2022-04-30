package xyz.mainframegames.kalabot.utils;

/** Enum that contains the different error messages that can be sent to the user */
public enum BotError {
  TECHNICAL_ERROR("Technical error", "There was a technical error"),
  AVATAR("Avatar command error", "The syntax of the `avatar` command is: `avatar [@person]`"),
  RATE("Rate command error", "The syntax of the `rate` command is: `rate [@person]`"),
  PLAY(
      "Play command error",
      "The syntax of the `play` command is: `play [Youtube Link or Video Name]`"),
  COORDINATE_ADD("Add error", "The syntax of the `add` command is: `add [name] [x] [y] [z]`"),
  COORDINATE_ALREADY_EXISTS(
      "Add error", "The coordinate name or positions introduced already exist"),
  COORDINATE_REMOVE("remove error", "The syntax of the `remove` command is: `remove [name]`"),
  COORDINATE_DOESNT_EXIST("remove error", "The name introduced isn't in the coordinates list"),
  COORDINATE_SHOW_ALL("show error", "The syntax of the `show` commands is: `show [page number]`"),
  COORDINATE_PAGE_DOESNT_EXIST("show error", "There's not a page with that number"),
  NO_COORDINATES_ADDED("show error", "There's no coordinates added to the list"),
  BOT_PERMISSION_ERROR("", "Not enough permissions"),
  NOT_PLAYING_MUSIC("", "I'm not currently playing music"),
  BOT_NOT_IN_A_VOICE_CHANNEL("", "I'm not in a voice channel"),
  USER_NOT_IN_A_VOICE_CHANNEL("", "You're not in a voice channel"),
  NOT_IN_SAME_VOICE_CHANNEL("", "You are not in the same voice channel as the bot"),
  AUDIO_TRACK_EXCEPTION("", "An error has occurred and the track couldn't be played"),

  NOT_A_COMMAND("Not a command", "That's not a command, try with `-help`");

  private final String error;
  private final String description;

  BotError(final String error, final String description) {
    this.error = error;
    this.description = description;
  }

  public String getError() {
    return error;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return error + " " + description;
  }
}
