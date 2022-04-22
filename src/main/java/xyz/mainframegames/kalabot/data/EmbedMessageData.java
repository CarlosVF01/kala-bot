package xyz.mainframegames.kalabot.data;

import java.awt.Color;
import lombok.Builder;
import lombok.Data;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageAuthor;

@Data
@Builder
public class EmbedMessageData {

  private MessageAuthor author;
  private String title;
  private String description;
  private String footer;
  private Icon thumbnail;
  private Color color;
}
