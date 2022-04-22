package xyz.mainframegames.kalabot.utils;

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
