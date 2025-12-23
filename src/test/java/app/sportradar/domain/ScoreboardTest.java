package app.sportradar.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

class ScoreboardTest {
  @Test
  @DisplayName("creates default score board instance.")
  void createsDefaultScoreWhenNullProvided() {
    Scoreboard scoreboard = new Scoreboard();

    assertNotNull(scoreboard);
  }

  @Test
  @DisplayName("starts a match")
  void startsMatch() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime now = LocalDateTime.now();
    scoreboard.startMatch("home", "away", now);

    assertNotNull(scoreboard);
    assertNotNull(scoreboard.findMatch("home", "away", now));
  }

  @Test
  @DisplayName("removes a match")
  void removesMatch() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    scoreboard.finishMatch(match);

    assertNotNull(scoreboard);
    assertNull(scoreboard.findMatch("home", "away", match.getStartTime()));
  }
}
