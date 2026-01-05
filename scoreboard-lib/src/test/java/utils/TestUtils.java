package utils;

import app.sportradar.domain.Match;
import app.sportradar.domain.Score;
import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
  public static Match createValidMatch() {
    LocalDateTime startTime = LocalDateTime.now().plusHours(1);
    return Match.builder()
        .homeTeam(" Home ")
        .awayTeam(" Away ")
        .score(Score.initial())
        .startTime(startTime)
        .build();
  }
}
