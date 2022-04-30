package xyz.mainframegames.kalabot.data;

import java.awt.Color;
import lombok.Builder;
import lombok.Data;
import org.javacord.api.entity.message.MessageAuthor;

/** Data class with the data required to create a basic embed */
@Data
@Builder
public class EmbedMessageData {

  private MessageAuthor author;
  private String title;
  private String description;
  private String footer;
  private String thumbnail;
  private Color color;
}
