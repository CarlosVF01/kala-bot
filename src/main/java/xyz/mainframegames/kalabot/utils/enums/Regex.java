package xyz.mainframegames.kalabot.utils.enums;

//Enum with all the regex patterns required for the bot to work
public enum Regex {
    //Regex pattern for formatted user ID
    MENTION_REGEX("(<@.?[0-9]*?>)"),
    HTTPS_URL_REGEX("https://www\\.youtube\\.com/watch\\?v=([a-zA-Z]+(\\d[a-zA-Z]+)+)")
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
