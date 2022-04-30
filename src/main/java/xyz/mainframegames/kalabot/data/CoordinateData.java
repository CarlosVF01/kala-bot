package xyz.mainframegames.kalabot.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Data class with the attributes necessary to add minecraft coordinates */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateData {

  private String coordinateName;
  private String coordinateX;
  private String coordinateY;
  private String coordinateZ;
  private String dateOfAddition;
}
