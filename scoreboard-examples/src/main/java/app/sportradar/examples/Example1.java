package app.sportradar.examples;

import app.sportradar.domain.Match;
import app.sportradar.domain.Scoreboard;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Example1 {

  public static void main(String[] args) {
    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime baseTime = LocalDateTime.of(2026, 6, 15, 14, 0);

    log.info("=== World Cup Live Scoreboard Demo ===\n");

    // Start matches
    log.info("Starting matches...\n");
    scoreboard.startMatch("Mexico", "Canada", baseTime);
    scoreboard.startMatch("Spain", "Brazil", baseTime.plusMinutes(15));
    scoreboard.startMatch("Germany", "France", baseTime.plusMinutes(30));
    scoreboard.startMatch("Uruguay", "Italy", baseTime.plusMinutes(45));
    scoreboard.startMatch("Argentina", "Australia", baseTime.plusHours(1));

    // Update scores
    log.info("Updating scores...\n");
    scoreboard.incrementAwayScoreByValue("Mexico", "Canada", baseTime, 5);

    scoreboard.incrementHomeScoreByValue("Spain", "Brazil", baseTime.plusMinutes(15), 10);
    scoreboard.incrementAwayScoreByValue("Spain", "Brazil", baseTime.plusMinutes(15), 2);

    scoreboard.incrementHomeScoreByValue("Germany", "France", baseTime.plusMinutes(30), 2);
    scoreboard.incrementAwayScoreByValue("Germany", "France", baseTime.plusMinutes(30), 2);

    scoreboard.incrementHomeScoreByValue("Uruguay", "Italy", baseTime.plusMinutes(45), 6);
    scoreboard.incrementAwayScoreByValue("Uruguay", "Italy", baseTime.plusMinutes(45), 6);

    scoreboard.incrementHomeScoreByValue("Argentina", "Australia", baseTime.plusHours(1), 3);
    scoreboard.incrementAwayScoreByValue("Argentina", "Australia", baseTime.plusHours(1), 1);

    // Get summary
    log.info("=== Live Scoreboard Summary ===");
    log.info("(Ordered by total score, then by most recent start time)\n");

    List<Match> summary = scoreboard.getSummary();
    int position = 1;
    for (Match match : summary) {
      log.info(
          "{}. {} {} - {} {}",
          position++,
          match.getHomeTeam(),
          match.getScore().homeScore(),
          match.getAwayTeam(),
          match.getScore().awayScore());
    }

    // Finish a match
    log.info("\n--- Match finished: Mexico vs Canada ---\n");
    scoreboard.finishMatch("Mexico", "Canada", baseTime);

    log.info("=== Updated Scoreboard ===\n");
    summary = scoreboard.getSummary();
    position = 1;
    for (Match match : summary) {
      log.info(
          "{}. {} {} - {} {}",
          position++,
          match.getHomeTeam(),
          match.getScore().homeScore(),
          match.getAwayTeam(),
          match.getScore().awayScore());
    }
  }
}
