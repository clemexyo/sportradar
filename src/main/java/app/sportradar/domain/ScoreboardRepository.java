package app.sportradar.domain;

/** Abstraction for persisting and retrieving the Scoreboard aggregate. */
public interface ScoreboardRepository {
  /** Returns the current scoreboard instance stored by the repository. */
  Scoreboard getScoreboard();

  /** Persists the provided scoreboard instance, replacing any previously stored value. */
  void save(Scoreboard scoreboard);

  /** Resets the repository to a brand-new scoreboard instance. */
  void clear();
}
