package app.sportradar.adapter.out;

import static org.junit.jupiter.api.Assertions.*;

import app.sportradar.domain.Match;
import app.sportradar.domain.Scoreboard;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestUtils;

class InMemoryScoreboardRepositoryTest {
  @Test
  @DisplayName("getScoreboard returns the same scoreboard instance until replaced")
  void getScoreboardReturnsSameInstance() {
    InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
    Scoreboard scoreboard = repository.getScoreboard();
    Match match = TestUtils.createValidMatch();
    scoreboard.startMatch(match);

    Scoreboard retrieved = repository.getScoreboard();

    assertSame(scoreboard, retrieved);
    assertNotNull(retrieved.findMatch(match));
  }

  @Test
  @DisplayName("save replaces the currently stored scoreboard instance")
  void saveReplacesStoredScoreboard() {
    InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
    Scoreboard replacement = new Scoreboard();
    LocalDateTime start = LocalDateTime.now();
    replacement.startMatch("A", "B", start);

    repository.save(replacement);

    assertSame(replacement, repository.getScoreboard());
    assertNotNull(repository.getScoreboard().findMatch("A", "B", start));
  }

  @Test
  @DisplayName("clear resets the repository to a fresh scoreboard instance")
  void clearResetsScoreboard() {
    InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();
    Scoreboard scoreboard = repository.getScoreboard();
    Match match = TestUtils.createValidMatch();
    scoreboard.startMatch(match);

    repository.clear();

    Scoreboard cleared = repository.getScoreboard();
    assertNotSame(scoreboard, cleared);
    assertNull(cleared.findMatch(match));
  }

  @Test
  @DisplayName("save rejects null scoreboards")
  void saveRejectsNullScoreboards() {
    InMemoryScoreboardRepository repository = new InMemoryScoreboardRepository();

    assertThrows(IllegalArgumentException.class, () -> repository.save(null));
  }
}
