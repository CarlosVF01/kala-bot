package xyz.mainframegames.kalabot.utils;

/**
 * Class with all emojis used
 */
public enum Emoji {
  THUMBS_DOWN("👎"),
  SAD_FACE("😔");

  private final String emojiString;

  Emoji(String emojiString) {
    this.emojiString = emojiString;
  }

  @Override
  public String toString() {
    return emojiString;
  }
}
