package app.sportradar.adapter.out;

import app.sportradar.domain.Scoreboard;
import app.sportradar.domain.ScoreboardRepository;
import java.util.concurrent.atomic.AtomicReference;

/** Simple in-memory implementation that keeps a single Scoreboard instance in memory. */
public class InMemoryScoreboardRepository implements ScoreboardRepository {
  private final AtomicReference<Scoreboard> scoreboardRef = new AtomicReference<>(new Scoreboard());

  @Override
  public Scoreboard getScoreboard() {
    return scoreboardRef.get();
  }

  @Override
  public void save(Scoreboard scoreboard) {
    if (scoreboard == null) {
      throw new IllegalArgumentException("Scoreboard must not be null.");
    }
    scoreboardRef.set(scoreboard);
  }

  @Override
  public void clear() {
    scoreboardRef.set(new Scoreboard());
  }
}
