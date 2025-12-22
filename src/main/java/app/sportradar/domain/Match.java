package app.sportradar.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Match {
  private final String homeTeam;
  private final String awayTeam;
  private final Score score;
  private final LocalDateTime startTime;

  @Builder(toBuilder = true)
  private Match(String homeTeam, String awayTeam, Score score, LocalDateTime startTime) {
    validateStrings(homeTeam, awayTeam);

    if (startTime == null) {
      throw new IllegalArgumentException("Start time cannot be null.");
    }

    if (homeTeam.trim().equalsIgnoreCase(awayTeam.trim())) {
      throw new IllegalArgumentException("A team cannot play against itself.");
    }

    this.homeTeam = homeTeam.trim();
    this.awayTeam = awayTeam.trim();
    this.score = (score == null) ? Score.initial() : score;
    this.startTime = startTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(homeTeam, awayTeam, startTime);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Match match = (Match) o;
    return homeTeam.equals(match.homeTeam)
        && awayTeam.equals(match.awayTeam)
        && startTime.equals(match.startTime);
  }

  private void validateStrings(String home, String away) {
    if (home == null || home.isBlank()) {
      throw new IllegalArgumentException("Home team name must not be blank.");
    }
    if (away == null || away.isBlank()) {
      throw new IllegalArgumentException("Away team name must not be blank.");
    }
  }
}
