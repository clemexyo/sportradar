package app.sportradar.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Match validation")
class MatchTest {

  @Test
  @DisplayName("creates default score when score is null")
  void createsDefaultScoreWhenNullProvided() {

    Match match = createValidMatch();

    assertEquals("Home", match.getHomeTeam());
    assertEquals("Away", match.getAwayTeam());
    assertEquals(0, match.getScore().homeScore());
    assertEquals(0, match.getScore().awayScore());
    assertTrue(match.getStartTime().isAfter(LocalDateTime.now()));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @DisplayName("home team must not be blank")
  void homeTeamMustNotBeBlank(String invalid) {
    Match.MatchBuilder matchBuilder = createValidMatch().toBuilder().homeTeam(invalid);
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, matchBuilder::build);

    assertEquals("Home team name must not be blank.", exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  @DisplayName("away team must not be blank")
  void awayTeamMustNotBeBlank(String invalid) {
    Match.MatchBuilder matchBuilder = createValidMatch().toBuilder().awayTeam(invalid);
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, matchBuilder::build);

    assertEquals("Away team name must not be blank.", exception.getMessage());
  }

  @Test
  @DisplayName("validation rejects null start time")
  void validationRejectsNullStartTime() {
    Match.MatchBuilder matchBuilder = createValidMatch().toBuilder().startTime(null);
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, matchBuilder::build);

    assertEquals("Start time cannot be null.", exception.getMessage());
  }

  @Test
  @DisplayName("validation rejects same team names ignoring case and whitespace")
  void validationRejectsSameTeams() {
    Match.MatchBuilder matchBuilder =
        createValidMatch().toBuilder().homeTeam(" Team ").awayTeam("team");
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, matchBuilder::build);

    assertEquals("A team cannot play against itself.", exception.getMessage());
  }

  @Test
  @DisplayName("equals/hashCode treat matches with same teams and start time as identical")
  void equalsAndHashCodeConsiderTeamsAndStartTime() {
    Match baseMatch =
        Match.builder()
            .homeTeam("Home")
            .awayTeam("Away")
            .score(Score.initial())
            .startTime(LocalDateTime.of(2024, 1, 1, 12, 0))
            .build();

    Match sameIdentifiersDifferentScore = baseMatch.toBuilder().score(new Score(5, 4)).build();

    assertEquals(baseMatch, sameIdentifiersDifferentScore);
    assertEquals(baseMatch.hashCode(), sameIdentifiersDifferentScore.hashCode());
  }

  @Test
  @DisplayName("equals/hashCode differ when a team or start time changes")
  void equalsAndHashCodeDifferWhenKeyFieldsChange() {
    Match reference = createValidMatch();

    Match differentTeam = reference.toBuilder().homeTeam("Different").build();
    Match differentStart =
        reference.toBuilder().startTime(reference.getStartTime().plusHours(2)).build();

    assertNotEquals(reference, differentTeam);
    assertNotEquals(reference.hashCode(), differentTeam.hashCode());

    assertNotEquals(reference, differentStart);
    assertNotEquals(reference.hashCode(), differentStart.hashCode());
  }

  private Match createValidMatch() {
    LocalDateTime startTime = LocalDateTime.now().plusHours(1);
    return Match.builder()
        .homeTeam(" Home ")
        .awayTeam(" Away ")
        .score(Score.initial())
        .startTime(startTime)
        .build();
  }
}
