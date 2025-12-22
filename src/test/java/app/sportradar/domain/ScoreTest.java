package app.sportradar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("incrementHome returns a new instance with only home score incremented")
    void incrementHomeReturnsNewScoreWithHomeTeamScoreIncremented() {
        Score original = new Score(2, 3);

        Score incremented = original.incrementHomeTeamScore();

        assertNotSame(original, incremented);
        assertEquals(3, incremented.homeScore());
        assertEquals(original.awayScore(), incremented.awayScore());
        assertEquals(2, original.homeScore(), "Original instance must remain unchanged");
    }

    @Test
    @DisplayName("incrementAway returns a new instance with only away score incremented")
    void incrementAwayReturnsNewScoreWithAwayTeamScoreIncremented() {
        Score original = new Score(4, 1);

        Score incremented = original.incrementAwayTeamScore();

        assertNotSame(original, incremented);
        assertEquals(original.homeScore(), incremented.homeScore());
        assertEquals(2, incremented.awayScore());
        assertEquals(1, original.awayScore(), "Original instance must remain unchanged");
    }

    @Test
    @DisplayName("Score.total return the sum score of two teams")
    void successfullyReturnsTotalScore() {
        Score score = new Score(4, 1);

        assertEquals(5, score.total());
    }


    @DisplayName("Throws IllegalArgumentException when home team score values out of bounds.")
    @ParameterizedTest
    @CsvSource({
            "10000, 'Home team score exceeds maximum allowed.'",
            "-5,   'Home team score cannot be negative.'"
    })    void throwsExceptionWhenHomeTeamScoreValueIsNotValid(int homeScore, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
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
    })    void throwsExceptionWhenAwayTeamScoreValueIsNotValid(int awayScore, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Score(5, awayScore) // just a random valid homeScore here.
        );

        assertEquals(expectedMessage, exception.getMessage());
    }


}
