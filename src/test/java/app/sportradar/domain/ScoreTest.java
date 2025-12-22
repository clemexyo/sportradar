package app.sportradar.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Score")
class ScoreTest {

  @Test
  @DisplayName("initial scores are zero for both teams")
  void initialScoreStartsAtZeroZero() {
    Score score = Score.initial();

    assertEquals(0, score.homeScore());
    assertEquals(0, score.awayScore());
  }

  @Test
  @DisplayName("incrementHomeTeamScore returns a new instance with only home score incremented")
  void incrementHomeTeamScoreReturnsNewScoreWithHomeIncremented() {
    Score original = new Score(2, 3);

    Score incremented = original.incrementHomeTeamScore();

    assertNotSame(original, incremented);
    assertEquals(3, incremented.homeScore());
    assertEquals(original.awayScore(), incremented.awayScore());
    assertEquals(2, original.homeScore(), "Original instance must remain unchanged");
  }

  @Test
  @DisplayName("incrementAwayTeamScore returns a new instance with only away score incremented")
  void incrementAwayTeamScoreReturnsNewScoreWithAwayIncremented() {
    Score original = new Score(4, 1);

    Score incremented = original.incrementAwayTeamScore();

    assertNotSame(original, incremented);
    assertEquals(original.homeScore(), incremented.homeScore());
    assertEquals(2, incremented.awayScore());
    assertEquals(1, original.awayScore(), "Original instance must remain unchanged");
  }

  @Test
  @DisplayName("incrementHomeScoreByValue adds the provided amount and keeps away score unchanged")
  void incrementHomeScoreByValueAddsAmount() {
    Score original = new Score(10, 5);

    Score updated = original.incrementHomeScoreByValue(5);

    assertEquals(15, updated.homeScore());
    assertEquals(5, updated.awayScore());
    assertNotSame(original, updated);
  }

  @Test
  @DisplayName("incrementHomeScoreByValue rejects negative increments")
  void incrementHomeScoreByValueRejectsNegative() {
    Score score = new Score(2, 2);

    assertThrows(IllegalArgumentException.class, () -> score.incrementHomeScoreByValue(-1));
  }

  @Test
  @DisplayName("incrementAwayScoreByValue adds the provided amount and keeps home score unchanged")
  void incrementAwayScoreByValueAddsAmount() {
    Score original = new Score(1, 6);

    Score updated = original.incrementAwayScoreByValue(4);

    assertEquals(1, updated.homeScore());
    assertEquals(10, updated.awayScore());
    assertNotSame(original, updated);
  }

  @Test
  @DisplayName("incrementAwayScoreByValue rejects negative increments")
  void incrementAwayScoreByValueRejectsNegative() {
    Score score = new Score(3, 3);

    assertThrows(IllegalArgumentException.class, () -> score.incrementAwayScoreByValue(-2));
  }

  @Test
  @DisplayName("decrementHomeScore lowers the home score by one")
  void decrementHomeScoreLowersHomeScore() {
    Score original = new Score(7, 2);

    Score decremented = original.decrementHomeScore();

    assertEquals(6, decremented.homeScore());
    assertEquals(2, decremented.awayScore());
  }

  @Test
  @DisplayName("decrementHomeScore throws when result would be negative")
  void decrementHomeScoreDoesNotAllowNegativeScores() {
    Score score = new Score(0, 0);

    assertThrows(IllegalArgumentException.class, score::decrementHomeScore);
  }

  @Test
  @DisplayName("decrementAwayScore lowers the away score by one")
  void decrementAwayScoreLowersAwayScore() {
    Score original = new Score(6, 4);

    Score decremented = original.decrementAwayScore();

    assertEquals(6, decremented.homeScore());
    assertEquals(3, decremented.awayScore());
  }

  @Test
  @DisplayName("decrementAwayScore throws when result would be negative")
  void decrementAwayScoreDoesNotAllowNegativeScores() {
    Score score = new Score(1, 0);

    assertThrows(IllegalArgumentException.class, score::decrementAwayScore);
  }

  @Test
  @DisplayName("decrementHomeScoreByValue reduces the home score by the amount")
  void decrementHomeScoreByValueReducesScore() {
    Score original = new Score(10, 2);

    Score updated = original.decrementHomeScoreByValue(4);

    assertEquals(6, updated.homeScore());
    assertEquals(2, updated.awayScore());
  }

  @Test
  @DisplayName("decrementHomeScoreByValue rejects negative amounts")
  void decrementHomeScoreByValueRejectsNegativeAmounts() {
    Score score = new Score(5, 5);

    assertThrows(IllegalArgumentException.class, () -> score.decrementHomeScoreByValue(-2));
  }

  @Test
  @DisplayName("decrementHomeScoreByValue throws when result would be negative")
  void decrementHomeScoreByValueDoesNotAllowNegativeScore() {
    Score score = new Score(3, 3);

    assertThrows(IllegalArgumentException.class, () -> score.decrementHomeScoreByValue(4));
  }

  @Test
  @DisplayName("decrementAwayScoreByValue reduces the away score by the amount")
  void decrementAwayScoreByValueReducesScore() {
    Score original = new Score(5, 9);

    Score updated = original.decrementAwayScoreByValue(5);

    assertEquals(5, updated.homeScore());
    assertEquals(4, updated.awayScore());
  }

  @Test
  @DisplayName("decrementAwayScoreByValue rejects negative amounts")
  void decrementAwayScoreByValueRejectsNegativeAmounts() {
    Score score = new Score(6, 6);

    assertThrows(IllegalArgumentException.class, () -> score.decrementAwayScoreByValue(-3));
  }

  @Test
  @DisplayName("decrementAwayScoreByValue throws when result would be negative")
  void decrementAwayScoreByValueDoesNotAllowNegativeScore() {
    Score score = new Score(3, 2);

    assertThrows(IllegalArgumentException.class, () -> score.decrementAwayScoreByValue(3));
  }

  @Test
  @DisplayName("Score.total returns the sum score of two teams")
  void successfullyReturnsTotalScore() {
    Score score = new Score(4, 1);

    assertEquals(5, score.total());
  }

  @ParameterizedTest(name = "home={0}, away={1} -> draw={2}")
  @DisplayName("isDraw returns true only when home and away scores match")
  @CsvSource({"5, 5, true", "7, 2, false"})
  void isDrawChecksScoreEquality(int home, int away, boolean expected) {
    Score score = new Score(home, away);

    assertEquals(expected, score.isDraw());
  }

  @DisplayName("Throws IllegalArgumentException when home team score values out of bounds.")
  @ParameterizedTest
  @CsvSource({
    "10000, 'Home team score exceeds maximum allowed.'",
    "-5,   'Home team score cannot be negative.'"
  })
  void throwsExceptionWhenHomeTeamScoreValueIsNotValid(int homeScore, String expectedMessage) {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> new Score(homeScore, 5) // just a random valid awayScore here.
            );

    assertEquals(expectedMessage, exception.getMessage());
  }

  @DisplayName("Throws IllegalArgumentException when away team score values out of bounds.")
  @ParameterizedTest
  @CsvSource({
    "10000, 'Away team score exceeds maximum allowed.'",
    "-5,   'Away team score cannot be negative.'"
  })
  void throwsExceptionWhenAwayTeamScoreValueIsNotValid(int awayScore, String expectedMessage) {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> new Score(5, awayScore) // just a random valid homeScore here.
            );

    assertEquals(expectedMessage, exception.getMessage());
  }
}
