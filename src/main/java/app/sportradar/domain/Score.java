package app.sportradar.domain;

public record Score(int homeScore, int awayScore) {
    // constants for validation
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 999;
    private static final int MAX_TOTAL_SCORE = MAX_SCORE * 2;

    /**
     * This is the *only* way to create a Score so all validation happens here.
     * Every Score creation passes through this.
     */
    public Score {
        validateScore(homeScore, "Home team");
        validateScore(awayScore, "Away team");
        validateTotalScore(homeScore, awayScore);
    }

    /**
     * Just a quick way to initialize a fresh score.
     */
    public static Score initial() {
        return new Score(0, 0);
    }

    /**
     * Returns a new Score with home team score incremented by 1.
     * Follows immutability pattern - returns new instance.
     */
    public Score incrementHomeTeamScore() {
        return new Score(homeScore + 1, awayScore);
    }

    /**
     * Returns a new Score with away team score incremented by 1.
     * Follows immutability pattern - returns new instance.
     */
    public Score incrementAwayTeamScore() {
        return new Score(homeScore, awayScore + 1);
    }


    /**
     * Return the sum of home team and away team scores.
     */
    public int total() {
        return homeScore + awayScore;
    }

    private static void validateScore(int score, String teamType) {
        if (score < MIN_SCORE) {
            throw new IllegalArgumentException(teamType + " score cannot be negative.");

        }
        if (score > MAX_SCORE) {
            throw new IllegalArgumentException(teamType + " score exceeds maximum allowed.");
        }
    }


    private static void validateTotalScore(int homeScore, int awayScore) {
        int total = homeScore + awayScore;
        if (total > MAX_TOTAL_SCORE) {
            throw new IllegalArgumentException("Total score exceeds maximum allowed.");
        }

    }
}
