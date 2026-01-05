package app.sportradar.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
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
    Match incremented = scoreboard.incrementHomeScoreByValue(match, 2);
    assertEquals(2, incremented.getScore().homeScore());
    assertEquals(0, incremented.getScore().awayScore());
    assertNotSame(
        match,
        incremented,
        "even though credentials are the same these are different object references, one has updated state..");

    Match decremented = scoreboard.decrementHomeScore(incremented);
    assertEquals(1, decremented.getScore().homeScore());
    assertEquals(0, decremented.getScore().awayScore());
    assertSame(
        decremented,
        scoreboard.findMatch(match),
        "lookup by original match credentials should find the updated version.");
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

  @Test
  @DisplayName(
      "getSummary returns matches ordered by total score descending, then by start time descending.")
  void getSummaryReturnsMatchesOrderedByTotalScoreThenByStartTime() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime baseTime = LocalDateTime.of(2024, 1, 1, 12, 0);

    // Start matches in chronological order
    scoreboard.startMatch("Mexico", "Canada", baseTime);
    scoreboard.startMatch("Spain", "Brazil", baseTime.plusMinutes(1));
    scoreboard.startMatch("Germany", "France", baseTime.plusMinutes(2));
    scoreboard.startMatch("Uruguay", "Italy", baseTime.plusMinutes(3));
    scoreboard.startMatch("Argentina", "Australia", baseTime.plusMinutes(4));

    // Set scores
    scoreboard.incrementAwayScoreByValue("Mexico", "Canada", baseTime, 5); // 0-5, total 5

    scoreboard.incrementHomeScoreByValue("Spain", "Brazil", baseTime.plusMinutes(1), 10);
    scoreboard.incrementAwayScoreByValue(
        "Spain", "Brazil", baseTime.plusMinutes(1), 2); // 10-2, total 12

    scoreboard.incrementHomeScoreByValue("Germany", "France", baseTime.plusMinutes(2), 2);
    scoreboard.incrementAwayScoreByValue(
        "Germany", "France", baseTime.plusMinutes(2), 2); // 2-2, total 4

    scoreboard.incrementHomeScoreByValue("Uruguay", "Italy", baseTime.plusMinutes(3), 6);
    scoreboard.incrementAwayScoreByValue(
        "Uruguay", "Italy", baseTime.plusMinutes(3), 6); // 6-6, total 12

    scoreboard.incrementHomeScoreByValue("Argentina", "Australia", baseTime.plusMinutes(4), 3);
    scoreboard.incrementAwayScoreByValue(
        "Argentina", "Australia", baseTime.plusMinutes(4), 1); // 3-1, total 4

    List<Match> summary = scoreboard.getSummary();

    assertEquals(5, summary.size());
    // Total 12: Uruguay started later than Spain, so Uruguay comes first
    assertEquals("Uruguay", summary.get(0).getHomeTeam());
    assertEquals("Spain", summary.get(1).getHomeTeam());
    // Total 5
    assertEquals("Mexico", summary.get(2).getHomeTeam());
    // Total 4: Argentina started later than Germany, so Argentina comes first
    assertEquals("Argentina", summary.get(3).getHomeTeam());
    assertEquals("Germany", summary.get(4).getHomeTeam());
  }
}
