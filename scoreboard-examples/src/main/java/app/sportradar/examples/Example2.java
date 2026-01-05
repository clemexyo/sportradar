package app.sportradar.examples;

import app.sportradar.domain.Match;
import app.sportradar.domain.Score;
import app.sportradar.domain.Scoreboard;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Example2 {

  public static void main(String[] args) {
    log.info("=== Scoreboard Feature Showcase ===\n");

    Scoreboard scoreboard = new Scoreboard();
    LocalDateTime matchTime = LocalDateTime.of(2026, 7, 10, 20, 0);

    // Feature 1: Start match using Match object with builder pattern
    log.info("--- Feature 1: Creating match with Builder pattern ---");
    Match match =
        Match.builder()
            .homeTeam("Real Madrid")
            .awayTeam("Barcelona")
            .startTime(matchTime)
            .score(Score.initial())
            .build();
    scoreboard.startMatch(match);
    log.info("Started: {} vs {}", match.getHomeTeam(), match.getAwayTeam());

    // Feature 2: Increment scores one by one
    log.info("\n--- Feature 2: Increment scores one at a time ---");
    Match updated = scoreboard.incrementHomeTeamScore(match);
    log.info("Goal! {} 1 - 0 {}", updated.getHomeTeam(), updated.getAwayTeam());

    updated = scoreboard.incrementAwayTeamScore(updated);
    log.info("Goal! {} 1 - 1 {}", updated.getHomeTeam(), updated.getAwayTeam());

    updated = scoreboard.incrementHomeTeamScore(updated);
    log.info("Goal! {} 2 - 1 {}", updated.getHomeTeam(), updated.getAwayTeam());

    // Feature 3: Demonstrating immutability
    log.info("\n--- Feature 3: Immutability demonstration ---");
    log.info(
        "Original match object score: {} - {}",
        match.getScore().homeScore(),
        match.getScore().awayScore());
    log.info(
        "Updated match object score: {} - {}",
        updated.getScore().homeScore(),
        updated.getScore().awayScore());
    log.info("Original match is unchanged (immutable design)");

    // Feature 4: Find match by identifiers
    log.info("\n--- Feature 4: Find match by team names and time ---");
    Match found = scoreboard.findMatch("Real Madrid", "Barcelona", matchTime);
    if (found != null) {
      log.info(
          "Found match: {} {} - {} {}",
          found.getHomeTeam(),
          found.getScore().homeScore(),
          found.getAwayTeam(),
          found.getScore().awayScore());
    }

    // Feature 5: Update score with absolute values (new feature!)
    log.info("\n--- Feature 5: Set absolute score values ---");
    log.info("VAR Review: Goal disallowed! Correcting score...");
    Match corrected = scoreboard.updateScore("Real Madrid", "Barcelona", matchTime, 1, 1);
    log.info(
        "Corrected score: {} {} - {} {}",
        corrected.getHomeTeam(),
        corrected.getScore().homeScore(),
        corrected.getAwayTeam(),
        corrected.getScore().awayScore());

    // Feature 6: Decrement score
    log.info("\n--- Feature 6: Decrement score ---");
    scoreboard.updateScore(corrected, 3, 2); // Set to 3-2 first
    log.info("Score is now 3-2");
    Match afterDecrement =
        scoreboard.decrementHomeScore(scoreboard.findMatch("Real Madrid", "Barcelona", matchTime));
    log.info(
        "After decrement: {} {} - {} {}",
        afterDecrement.getHomeTeam(),
        afterDecrement.getScore().homeScore(),
        afterDecrement.getAwayTeam(),
        afterDecrement.getScore().awayScore());

    // Feature 7: Multiple matches and summary ordering
    log.info("\n--- Feature 7: Multiple matches with summary ordering ---");
    scoreboard.startMatch("Liverpool", "Man City", matchTime.plusHours(1));
    scoreboard.startMatch("PSG", "Bayern", matchTime.plusHours(2));

    scoreboard.updateScore("Liverpool", "Man City", matchTime.plusHours(1), 4, 4);
    scoreboard.updateScore("PSG", "Bayern", matchTime.plusHours(2), 1, 0);

    log.info("\nLive Scoreboard (ordered by total score, then by start time):");
    List<Match> summary = scoreboard.getSummary();
    int position = 1;
    for (Match m : summary) {
      log.info(
          "{}. {} {} - {} {} (total: {})",
          position++,
          m.getHomeTeam(),
          m.getScore().homeScore(),
          m.getAwayTeam(),
          m.getScore().awayScore(),
          m.getScore().total());
    }

    // Feature 8: Finish matches
    log.info("\n--- Feature 8: Finish matches ---");
    scoreboard.finishMatch("Real Madrid", "Barcelona", matchTime);
    log.info("Match finished: Real Madrid vs Barcelona");

    log.info("\nRemaining matches:");
    summary = scoreboard.getSummary();
    for (Match m : summary) {
      log.info(
          "  {} {} - {} {}",
          m.getHomeTeam(),
          m.getScore().homeScore(),
          m.getAwayTeam(),
          m.getScore().awayScore());
    }

    // Feature 9: Error handling demonstration
    log.info("\n--- Feature 9: Error handling ---");
    try {
      scoreboard.updateScore("Nonexistent", "Team", matchTime, 1, 0);
    } catch (IllegalArgumentException e) {
      log.info("Caught expected error: {}", e.getMessage());
    }

    try {
      Match existingMatch = scoreboard.findMatch("Liverpool", "Man City", matchTime.plusHours(1));
      scoreboard.updateScore(existingMatch, -1, 0);
    } catch (IllegalArgumentException e) {
      log.info("Caught expected error: {}", e.getMessage());
    }

    log.info("\n=== Showcase Complete ===");
  }
}
