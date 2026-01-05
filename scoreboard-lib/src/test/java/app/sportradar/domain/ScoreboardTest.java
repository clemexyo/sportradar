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
    LocalDateTime baseTime = LocalDateTime.of(2026, 1, 1, 12, 0);

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

  @Test
  @DisplayName("incrementHomeTeamScore using identifiers updates and persists the match.")
  void incrementHomeTeamScoreUsingIdentifiersUpdatesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);

    Match updated = scoreboard.incrementHomeTeamScore("Home", "Away", start);

    assertEquals(1, updated.getScore().homeScore());
    assertEquals(0, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch("Home", "Away", start));
  }

  @Test
  @DisplayName("incrementAwayTeamScore using Match updates and persists the match.")
  void incrementAwayTeamScoreUsingMatchUpdatesScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);

    Match updated = scoreboard.incrementAwayTeamScore(match);

    assertEquals(0, updated.getScore().homeScore());
    assertEquals(1, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("incrementAwayTeamScore using identifiers updates and persists the match.")
  void incrementAwayTeamScoreUsingIdentifiersUpdatesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);

    Match updated = scoreboard.incrementAwayTeamScore("Home", "Away", start);

    assertEquals(0, updated.getScore().homeScore());
    assertEquals(1, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch("Home", "Away", start));
  }

  @Test
  @DisplayName("incrementHomeScoreByValue using identifiers applies the provided amount.")
  void incrementHomeScoreByValueUsingIdentifiersUpdatesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);

    Match updated = scoreboard.incrementHomeScoreByValue("Home", "Away", start, 5);

    assertEquals(5, updated.getScore().homeScore());
    assertEquals(0, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch("Home", "Away", start));
  }

  @Test
  @DisplayName("incrementAwayScoreByValue using Match applies the provided amount.")
  void incrementAwayScoreByValueUsingMatchUpdatesScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);

    Match updated = scoreboard.incrementAwayScoreByValue(match, 4);

    assertEquals(0, updated.getScore().homeScore());
    assertEquals(4, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("decrementHomeScore using identifiers reduces the score.")
  void decrementHomeScoreUsingIdentifiersReducesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);
    scoreboard.incrementHomeScoreByValue("Home", "Away", start, 3);

    Match decremented = scoreboard.decrementHomeScore("Home", "Away", start);

    assertEquals(2, decremented.getScore().homeScore());
    assertEquals(0, decremented.getScore().awayScore());
  }

  @Test
  @DisplayName("decrementAwayScore using Match reduces the score and returns updated match.")
  void decrementAwayScoreUsingMatchReducesScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    Match incremented = scoreboard.incrementAwayScoreByValue(match, 2);

    Match decremented = scoreboard.decrementAwayScore(incremented);

    assertEquals(0, decremented.getScore().homeScore());
    assertEquals(1, decremented.getScore().awayScore());
    assertSame(decremented, scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("decrementAwayScore using identifiers reduces the score.")
  void decrementAwayScoreUsingIdentifiersReducesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);
    scoreboard.incrementAwayScoreByValue("Home", "Away", start, 3);

    Match decremented = scoreboard.decrementAwayScore("Home", "Away", start);

    assertEquals(0, decremented.getScore().homeScore());
    assertEquals(2, decremented.getScore().awayScore());
  }

  @Test
  @DisplayName("decrementHomeScoreByValue using Match reduces the score by amount.")
  void decrementHomeScoreByValueUsingMatchReducesScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    Match incremented = scoreboard.incrementHomeScoreByValue(match, 5);

    Match decremented = scoreboard.decrementHomeScoreByValue(incremented, 3);

    assertEquals(2, decremented.getScore().homeScore());
    assertEquals(0, decremented.getScore().awayScore());
  }

  @Test
  @DisplayName("decrementHomeScoreByValue using identifiers reduces the score by amount.")
  void decrementHomeScoreByValueUsingIdentifiersReducesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);
    scoreboard.incrementHomeScoreByValue("Home", "Away", start, 5);

    Match decremented = scoreboard.decrementHomeScoreByValue("Home", "Away", start, 2);

    assertEquals(3, decremented.getScore().homeScore());
    assertEquals(0, decremented.getScore().awayScore());
  }

  @Test
  @DisplayName("decrementAwayScoreByValue using Match reduces the score by amount.")
  void decrementAwayScoreByValueUsingMatchReducesScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    Match incremented = scoreboard.incrementAwayScoreByValue(match, 5);

    Match decremented = scoreboard.decrementAwayScoreByValue(incremented, 3);

    assertEquals(0, decremented.getScore().homeScore());
    assertEquals(2, decremented.getScore().awayScore());
  }

  @Test
  @DisplayName("decrementAwayScoreByValue using identifiers reduces the score by amount.")
  void decrementAwayScoreByValueUsingIdentifiersReducesScore() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);
    scoreboard.incrementAwayScoreByValue("Home", "Away", start, 5);

    Match decremented = scoreboard.decrementAwayScoreByValue("Home", "Away", start, 2);

    assertEquals(0, decremented.getScore().homeScore());
    assertEquals(3, decremented.getScore().awayScore());
  }

  @Test
  @DisplayName("finishMatch using identifiers removes the match from the board.")
  void finishMatchUsingIdentifiersRemovesMatch() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);

    scoreboard.finishMatch("Home", "Away", start);

    assertNull(scoreboard.findMatch("Home", "Away", start));
  }

  @Test
  @DisplayName("findMatch returns null when match does not exist.")
  void findMatchReturnsNullWhenMatchDoesNotExist() {
    Scoreboard scoreboard = new Scoreboard();
    Match match = TestUtils.createValidMatch();

    assertNull(scoreboard.findMatch(match));
    assertNull(scoreboard.findMatch("nonexistent", "teams", LocalDateTime.now()));
  }

  @Test
  @DisplayName("update operations throw exception when match is null.")
  void updateOperationsThrowWhenMatchIsNull() {
    Scoreboard scoreboard = new Scoreboard();

    assertThrows(IllegalArgumentException.class, () -> scoreboard.incrementHomeTeamScore(null));
  }

  @Test
  @DisplayName("getSummary returns empty list when scoreboard has no matches.")
  void getSummaryReturnsEmptyListWhenNoMatches() {
    Scoreboard scoreboard = new Scoreboard();

    List<Match> summary = scoreboard.getSummary();

    assertNotNull(summary);
    assertTrue(summary.isEmpty());
  }

  @Test
  @DisplayName("updateScore with Match sets absolute home and away scores.")
  void updateScoreWithMatchSetsAbsoluteScores() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);

    Match updated = scoreboard.updateScore(match, 3, 2);

    assertEquals(3, updated.getScore().homeScore());
    assertEquals(2, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch(match));
  }

  @Test
  @DisplayName("updateScore with identifiers sets absolute home and away scores.")
  void updateScoreWithIdentifiersSetsAbsoluteScores() {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    scoreboard.startMatch("Home", "Away", start);

    Match updated = scoreboard.updateScore("Home", "Away", start, 5, 4);

    assertEquals(5, updated.getScore().homeScore());
    assertEquals(4, updated.getScore().awayScore());
    assertSame(updated, scoreboard.findMatch("Home", "Away", start));
  }

  @Test
  @DisplayName("updateScore replaces previous score completely.")
  void updateScoreReplacesPreviousScore() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);
    scoreboard.incrementHomeScoreByValue(match, 10);

    Match updated = scoreboard.updateScore(match, 1, 1);

    assertEquals(1, updated.getScore().homeScore());
    assertEquals(1, updated.getScore().awayScore());
  }

  @Test
  @DisplayName("updateScore throws exception when match does not exist.")
  void updateScoreThrowsWhenMatchDoesNotExist() {
    Scoreboard scoreboard = new Scoreboard();
    Match match = TestUtils.createValidMatch();

    assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(match, 1, 0));
  }

  @Test
  @DisplayName("updateScore throws exception for negative scores.")
  void updateScoreThrowsForNegativeScores() {
    Match match = TestUtils.createValidMatch();
    Scoreboard scoreboard = new Scoreboard();
    scoreboard.startMatch(match);

    assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(match, -1, 0));
    assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore(match, 0, -1));
  }
}
