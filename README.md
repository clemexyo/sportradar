# Sportradar Live Scoreboard

A simple Java library for managing live football match scoreboards. Track ongoing matches, update scores, and get a summary ordered by total score.

## Project Structure

```
sportradar/
├── scoreboard-lib/       # Core library (the dependency you import)
└── scoreboard-examples/  # Usage examples
```

## Quick Start

### Build

```bash
mvn clean install
```

### Use as a Dependency

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>app.sportradar</groupId>
    <artifactId>scoreboard-lib</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Usage

```java
Scoreboard scoreboard = new Scoreboard();

// Start a match
scoreboard.startMatch("Mexico", "Canada", LocalDateTime.now());

// Update scores
scoreboard.incrementHomeTeamScore("Mexico", "Canada", startTime);
scoreboard.updateScore("Mexico", "Canada", startTime, 3, 2);  // Set absolute score

// Get live summary (ordered by total score, then most recent)
List<Match> summary = scoreboard.getSummary();

// Finish a match
scoreboard.finishMatch("Mexico", "Canada", startTime);
```

## Features

- **Start/Finish matches** - Add and remove matches from the scoreboard
- **Update scores** - Increment, decrement, or set absolute values
- **Live summary** - Get matches ordered by total score (highest first), with ties broken by most recently started
- **Immutable design** - Match and Score objects are immutable for thread safety
- **Validation** - Scores cannot be negative

## Run Examples

```bash
# Build first
mvn clean install

# Run Example1 - World Cup demo
java -cp "scoreboard-examples/target/scoreboard-examples-1.0-SNAPSHOT.jar:scoreboard-lib/target/scoreboard-lib-1.0-SNAPSHOT.jar:$HOME/.m2/repository/org/slf4j/slf4j-simple/2.0.17/slf4j-simple-2.0.17.jar:$HOME/.m2/repository/org/slf4j/slf4j-api/2.0.17/slf4j-api-2.0.17.jar" app.sportradar.examples.Example1

# Run Example2 - Feature showcase
java -cp "scoreboard-examples/target/scoreboard-examples-1.0-SNAPSHOT.jar:scoreboard-lib/target/scoreboard-lib-1.0-SNAPSHOT.jar:$HOME/.m2/repository/org/slf4j/slf4j-simple/2.0.17/slf4j-simple-2.0.17.jar:$HOME/.m2/repository/org/slf4j/slf4j-api/2.0.17/slf4j-api-2.0.17.jar" app.sportradar.examples.Example2
```

## Requirements

- Java 21+
- Maven 3.8+
