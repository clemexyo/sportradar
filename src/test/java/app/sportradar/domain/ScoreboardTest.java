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
    scoreboard.startMatch("home", "away", LocalDateTime.now());

    assertNotNull(scoreboard);
    assertNotNull(scoreboard.findMatch("home", "away", LocalDateTime.now()));
  }

  @Test
  @DisplayName("removes a match")
  void removesMatch() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    scoreboard.removeMatch(match);

    assertNotNull(scoreboard);
    assertNotNull(scoreboard.findMatch("home", "away", LocalDateTime.now()));
  }
}
