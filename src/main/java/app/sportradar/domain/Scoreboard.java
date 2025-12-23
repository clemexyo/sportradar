package app.sportradar.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
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

  public Match incrementHomeTeamScore(Match match) {
    return updateScore(match, Score::incrementHomeTeamScore);
  }

  public Match incrementHomeTeamScore(String home, String away, LocalDateTime date) {
    return updateScore(home, away, date, Score::incrementHomeTeamScore);
  }

  public Match incrementAwayTeamScore(Match match) {
    return updateScore(match, Score::incrementAwayTeamScore);
  }

  public Match incrementAwayTeamScore(String home, String away, LocalDateTime date) {
    return updateScore(home, away, date, Score::incrementAwayTeamScore);
  }

  public Match incrementHomeScoreByValue(Match match, int amount) {
    return updateScore(match, score -> score.incrementHomeScoreByValue(amount));
  }

  public Match incrementHomeScoreByValue(String home, String away, LocalDateTime date, int amount) {
    return updateScore(home, away, date, score -> score.incrementHomeScoreByValue(amount));
  }

  public Match incrementAwayScoreByValue(Match match, int amount) {
    return updateScore(match, score -> score.incrementAwayScoreByValue(amount));
  }

  public Match incrementAwayScoreByValue(String home, String away, LocalDateTime date, int amount) {
    return updateScore(home, away, date, score -> score.incrementAwayScoreByValue(amount));
  }

  public Match decrementHomeScore(Match match) {
    return updateScore(match, Score::decrementHomeScore);
  }

  public Match decrementHomeScore(String home, String away, LocalDateTime date) {
    return updateScore(home, away, date, Score::decrementHomeScore);
  }

  public Match decrementAwayScore(Match match) {
    return updateScore(match, Score::decrementAwayScore);
  }

  public Match decrementAwayScore(String home, String away, LocalDateTime date) {
    return updateScore(home, away, date, Score::decrementAwayScore);
  }

  public Match decrementHomeScoreByValue(Match match, int amount) {
    return updateScore(match, score -> score.decrementHomeScoreByValue(amount));
  }

  public Match decrementHomeScoreByValue(String home, String away, LocalDateTime date, int amount) {
    return updateScore(home, away, date, score -> score.decrementHomeScoreByValue(amount));
  }

  public Match decrementAwayScoreByValue(Match match, int amount) {
    return updateScore(match, score -> score.decrementAwayScoreByValue(amount));
  }

  public Match decrementAwayScoreByValue(String home, String away, LocalDateTime date, int amount) {
    return updateScore(home, away, date, score -> score.decrementAwayScoreByValue(amount));
  }

  private Match updateScore(Match match, UnaryOperator<Score> scoreUpdater) {
    Match storedMatch = requireMatch(match);
    return persistUpdatedMatch(storedMatch, scoreUpdater);
  }

  private Match updateScore(
      String home, String away, LocalDateTime date, UnaryOperator<Score> scoreUpdater) {
    Match storedMatch = requireMatch(home, away, date);
    return persistUpdatedMatch(storedMatch, scoreUpdater);
  }

  private Match persistUpdatedMatch(Match match, UnaryOperator<Score> scoreUpdater) {
    Score updatedScore = scoreUpdater.apply(match.getScore());
    Match updatedMatch = match.toBuilder().score(updatedScore).build();
    matchMap.put(updatedMatch.hashCode(), updatedMatch);
    return updatedMatch;
  }

  private Match requireMatch(Match match) {
    if (match == null) {
      throw new IllegalArgumentException("Match must not be null.");
    }
    Match storedMatch = findMatch(match);
    if (storedMatch == null) {
      throw new IllegalArgumentException("Match does not exist on the scoreboard.");
    }
    return storedMatch;
  }

  private Match requireMatch(String home, String away, LocalDateTime date) {
    Match storedMatch = findMatch(home, away, date);
    if (storedMatch == null) {
      throw new IllegalArgumentException("Match does not exist on the scoreboard.");
    }
    return storedMatch;
  }
}
