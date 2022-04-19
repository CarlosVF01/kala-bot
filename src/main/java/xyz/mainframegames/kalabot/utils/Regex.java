package xyz.mainframegames.kalabot.utils;

/**
 * Enum with all the regex used through the whole application
 */
public enum Regex {

  /**
   * User ID mention regex
   */
  MENTION_REGEX("(<@.?[0-9]*?>)"),
  /**
   * Regex for youtube https links
   */
  HTTPS_URL_REGEX("https://www.youtube.com/watch?v=([a-zA-Z]+(d[a-zA-Z]+)+)"),
  ANY_CHARACTER_REGEX("[a-zA-Z]+");

  private final String text;

  Regex(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
