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
  @DisplayName("starts a match with given data.")
  void startsMatchWithGivenData() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime now = LocalDateTime.now();
    scoreboard.startMatch("home", "away", now);

    assertNotNull(scoreboard);
    assertNotNull(scoreboard.findMatch("home", "away", now));
  }

  @Test
  @DisplayName("adds a match to the board.")
  void addsMatchToTheBoard() {
    Scoreboard scoreboard = new Scoreboard();
    Match match = TestUtils.createValidMatch();
    scoreboard.startMatch(match);

    assertNotNull(scoreboard);
    assertNotNull(scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("removes a match with the given data.")
  void removesMatchWithGivenData() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    scoreboard.finishMatch(match);

    assertNotNull(scoreboard);
    assertNull(scoreboard.findMatch("home", "away", match.getStartTime()));
  }

  @Test
  @DisplayName("removes a match from the board.")
  void removesMatchFromTheBoard() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    scoreboard.finishMatch(match);

    assertNotNull(scoreboard);
    assertNull(scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("incrementHomeTeamScore updates the stored match score.")
  void incrementHomeTeamScoreUpdatesStoredMatchScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);

    Match updated = scoreboard.incrementHomeTeamScore(match);

    assertEquals(1, updated.getScore().homeScore());
    assertEquals(0, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("incrementAwayScoreByValue using identifiers applies the provided amount.")
  void incrementAwayScoreByValueUsingIdentifiersUpdatesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Team A", "Team B", start);

    Match updated = scoreboard.incrementAwayScoreByValue("Team A", "Team B", start, 3);

    assertEquals(0, updated.getScore().homeScore());
    assertEquals(3, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch("Team A", "Team B", start));
  }

  @Test
  @DisplayName("decrementHomeScore reduces the score and persists the updated match.")
  void decrementHomeScoreReducesScoreAndPersistsMatch() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    scoreboard.incrementHomeScoreByValue(match, 2);

    Match decremented = scoreboard.decrementHomeScore(match);

    assertEquals(1, decremented.getScore().homeScore());
    assertEquals(0, decremented.getScore().awayScore());
    assertSame(decremented, scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("increment or decrement operations require the match to exist on the board.")
  void updateOperationsRequireMatchToExist() {
    Scoreboard scoreboard = new Scoreboard();
    Match match = TestUtils.createValidMatch();

    assertThrows(IllegalArgumentException.class, () -> scoreboard.incrementHomeTeamScore(match));
  }

  @Test
  @DisplayName("decrement operations reuse Score validation and reject negative results.")
  void decrementOperationsRespectScoreValidation() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);

    assertThrows(IllegalArgumentException.class, () -> scoreboard.decrementAwayScore(match));
  }
}
