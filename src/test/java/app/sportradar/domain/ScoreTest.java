package app.sportradar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

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
}
