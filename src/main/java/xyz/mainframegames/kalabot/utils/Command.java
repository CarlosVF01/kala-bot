package xyz.mainframegames.kalabot.utils;

/**
 * Enum with all the commands and descriptions to have them grouped in one place
 */
public enum Command {

  RATE("rate", "Rates someone in a scale of 10"),
  PLAY("play", "Plays a youtube video"),
  PAUSE("pause", "Pauses the audio track"),
  SKIP("skip", "Skips the current audio track"),
  RESUME("resume", "Resumes the current audio track"),
  LEAVE("leave", "Makes the bot leave the voice channel"),
  AVATAR("avatar", "Shows your avatar"),
  HELP("help", "Shows the list of available commands");

  private static final String COMMAND_PREFIX = "-";
  private final String commandInput;
  private final String commandDescription;

  Command(final String commandInput, String commandDescription) {
    this.commandInput = COMMAND_PREFIX + commandInput;
    this.commandDescription = commandDescription;
  }

  public String getCommandInput() {
    return commandInput;
  }

  public String getCommandDescription() {
    return commandDescription;
  }

  @Override
  public String toString() {
    return commandInput;
  }
}
