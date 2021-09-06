package xyz.mainframegames.kalabot.utils;

//Enum with all the regex patterns required for the bot to work
public enum Regex {
    //Regex pattern for formatted user ID
    MENTION_REGEX("(<@.?[0-9]*?>)"),
    ;
    private final String text;

    Regex(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
