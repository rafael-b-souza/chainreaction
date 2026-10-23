# Chain Reaction (Java)

Desktop implementation of the classic Chain Reaction board game in Java. The project includes a Swing UI, an AI plug-in architecture, and a tournament runner to evaluate artificial players against each other.

## Features

- **Gameplay**: Human vs Human, Human vs AI, and AI vs AI modes. Configurable board dimensions (default 6x5) and save/load state support (\`*.crsave\`).
- **Graphics**: Retro pixel UI, configurable animations, and explosion styles.
- **AI Engine**: Built-in default AI and support for external AI plugins via \`ServiceLoader\`.
- **Tournaments**: Command-line tournament runner to benchmark AIs across multiple rounds.
- **Tests**: JUnit test suite covering core game rules and save/load serialization.

## Project Structure

\`\`\`
chainreaction/             # Core game engine and Swing UI
└─ src/
   ├─ main/java/de/freewarepoint/cr/       # Game engine
   ├─ main/java/de/freewarepoint/cr/swing  # UI components and animation
   ├─ main/java/de/freewarepoint/cr/ai     # AI interfaces and standard AI
   ├─ main/java/de/freewarepoint/cr/io     # Save/load utilities
   └─ test/java/de/freewarepoint/cr/test   # JUnit tests
exampleAI/                 # Sample external AI plugin
\`\`\`

## Requirements

- Java JDK 8 or newer
- Maven 3 (or Gradle 7+)

## Build and Run

### Compiling with Maven

\`\`\`bash
git clone https://github.com/rafael-b-souza/chainreaction.git
cd chainreaction
mvn clean package
\`\`\`

### Running the Game

\`\`\`bash
java -jar target/chainreaction-<version>.jar
\`\`\`

Alternatively, use the included shell script:

\`\`\`bash
./run_chainreaction.sh
\`\`\`

### Running an AI Tournament

\`\`\`bash
# Pass the number of rounds per match as the first argument
java -cp target/chainreaction-<version>.jar de.freewarepoint.cr.tournament.TournamentRunner 100
\`\`\`

## Creating Custom AI Plugins

Implement the \`de.freewarepoint.cr.ai.AI\` interface:

\`\`\`java
public class CustomAI implements AI {
    private Game game;

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "CustomAI";
    }

    @Override
    public void doMove() {
        // Evaluate board state and select move
        game.selectMove(new CellCoordinateTuple(x, y));
    }
}
\`\`\`

1. Package the class into a JAR containing \`META-INF/services/de.freewarepoint.cr.ai.AI\`.
2. Place the JAR in \`~/.cr/AIs/\` (Linux/macOS) or \`%USERPROFILE%\\.cr\\AIs\\\` (Windows).
3. Select the custom AI from the in-game settings.

## Configuration

Settings are saved in \`~/.cr/settings.properties\` on first execution, covering delay timers, animation preferences, and plugin directories.

## Running Tests

\`\`\`bash
mvn test
\`\`\`

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
