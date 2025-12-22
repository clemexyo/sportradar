package app.sportradar.domain;

public record Score(int homeScore, int awayScore) {

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
}
