package app.sportradar.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class Scoreboard {
  private final Map<Integer, Match> matchMap = new HashMap<>();

  public Scoreboard() {}

  public Match findMatch(String home, String away, LocalDateTime date) {
    int hash = Match.builder().homeTeam(home).awayTeam(away).startTime(date).build().hashCode();
    return matchMap.get(hash);
  }

  public Match findMatch(Match match) {
    int hash = match.hashCode();
    return matchMap.get(hash);
  }

  public void finishMatch(String home, String away, LocalDateTime date) {
    int hash = Match.builder().homeTeam(home).awayTeam(away).startTime(date).build().hashCode();
    matchMap.remove(hash);
  }

  public void finishMatch(Match match) {
    matchMap.remove(match.hashCode());
  }

  public void startMatch(Match match) {
    matchMap.put(match.hashCode(), match);
  }

  public void startMatch(String home, String away, LocalDateTime date) {
    Match match = Match.builder().homeTeam(home).awayTeam(away).startTime(date).build();
    matchMap.put(match.hashCode(), match);
  }
}
