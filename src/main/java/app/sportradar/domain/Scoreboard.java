package app.sportradar.domain;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;

@Getter
public class Scoreboard {
  private Map<Integer, Match> matchMap;

  public Scoreboard() {}

  public Match findMatch(String home, String away, LocalDateTime date) {
    // TODO: implement
    return null;
  }

  public Match findMatch(Match match) {
    // TODO: implement
    return null;
  }

  public void removeMatch(String home, String away, LocalDateTime date) {
    // TODO: implement
  }

  public void removeMatch(Match match) {
    // TODO: implement
  }

  public Match startMatch(Match match) {
    // TODO: implement
    return null;
  }

  public void startMatch(String home, String away, LocalDateTime date) {}
}
