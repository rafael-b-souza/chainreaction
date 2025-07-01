# Chain Reaction (Java)

A retro‑styled desktop implementation of the classic **Chain Reaction** board‑game written entirely in Java. The project includes a full Swing UI, a pluggable AI architecture, and a tournament driver so you can pit artificial players against each other.

---

## ✨ Features

| Area       | Highlights                                                                                                                                |
| ---------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| Gameplay   | • Human vs Human, Human vs AI, or AI vs AI<br>• 6 × 5 default board (size configurable in code)<br>• Save/Load support (`*.crsave` files) |
| Graphics   | • Retro pixel‑font HUD & animations<br>• Smooth or classic explosion styles with customisable frame count                                 |
| AI         | • Simple built‑in **StandardAI**<br>• Drop‑in AI plug‑ins (JARs discovered via `ServiceLoader`)                                           |
| Tournament | • CLI **TournamentRunner** to benchmark AIs over *N* rounds                                                                               |
| Test Suite | • JUnit tests validate game logic & save/load cycle                                                                                       |

---

## 🗂  Project Layout

```
chainreaction/             # core game + Swing UI
└─ src/
   ├─ main/java/de/freewarepoint/cr/…     # game engine
   ├─ main/java/de/freewarepoint/cr/swing # UI widgets & animation
   ├─ main/java/de/freewarepoint/cr/ai    # AI SPI + built‑ins
   ├─ main/java/de/freewarepoint/cr/io    # save/load helpers
   └─ test/java/de/freewarepoint/cr/test  # JUnit tests
exampleAI/                 # sample external AI plug‑in
```

---

## 🔧 Requirements

* **JDK 8** or newer (only standard JDK APIs are used)
* **Maven 3** *(recommended)* or **Gradle 7+** to build

---

## 📦 Building & Running

### 1. Clone & compile

```bash
$ git clone https://github.com/rafael-b-souza/chainreaction.git chainreaction
$ cd chainreaction
$ mvn clean package      # produces target/chainreaction-<ver>.jar
```

*No Maven?* Replace with `gradle build` or see manual `javac` instructions at the end of this file.

### 2. Launch the GUI

```bash
$ java -jar target/chainreaction-<ver>.jar   # starts Swing UI in full‑screen
```

### 3. Run an AI tournament

```bash
# first argument = even number of rounds per match
$ java -cp target/chainreaction-<ver>.jar \
       de.freewarepoint.cr.tournament.TournamentRunner 100
```

---

## 🤖 Writing Your Own AI

Implement the `de.freewarepoint.cr.ai.AI` interface:

```java
public class MyAwesomeAI implements AI {
    private Game game;
    @Override public void setGame(Game g) { this.game = g; }
    @Override public String getName()      { return "Awesome"; }
    @Override public void doMove() {
        // …inspect game.getField(), decide move…
        game.selectMove(new CellCoordinateTuple(x, y));
    }
}
```

1. **Package** your AI into a JAR with a *services* entry:
   *Create* `META-INF/services/de.freewarepoint.cr.ai.AI` containing the full class name, e.g.

   ```
   com.example.MyAwesomeAI
   ```
2. **Drop** the JAR into your local AI folder:
   *Linux/macOS* `~/.cr/AIs/`    *Windows* `%USERPROFILE%\.cr\AIs\`
3. **Launch** the GUI → *Settings* → choose the AI for either player, or run a tournament.

---

## ⚙️ Configuration

| File                  | Location | Purpose                                       |
| --------------------- | -------- | --------------------------------------------- |
| `settings.properties` | `~/.cr/` | Reaction delay, animation style & frame count |
| `AIs/` (directory)    | `~/.cr/` | External AI plug‑ins (JARs)                   |

Change settings from inside the game or edit the file directly—it’s written automatically on first run.

---

## 🧪 Running Tests

```bash
$ mvn test   # executes GameTest suite
```

---

## 🙋‍♂️ Contributing

1. Fork & create a feature branch
2. Follow the existing coding style (formatter config in IDE files TBD)
3. Submit a PR—be sure tests still pass and *StandardAI* remains functional.

---

## 📜 License

This project is released under the MIT License. See [`LICENSE`](LICENSE) for details.

---

## 🗒 Manual Compile (optional)

```bash
# compile
$ javac -d out $(find chainreaction/src/main/java -name "*.java")
# run
$ java -cp out de.freewarepoint.cr.swing.UIGame
```

Enjoy blowing stuff up pixel‑style! 🎆
