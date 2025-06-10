package de.freewarepoint.cr;

import de.freewarepoint.cr.io.FileHandler;
import de.freewarepoint.cr.io.GameState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Game {
	
	private final Field field;
	private final Settings settings;
	private final List<MoveListener> listeners = new ArrayList<>();
	private Player player = Player.FIRST;
	private final Set<Player> moved = EnumSet.noneOf(Player.class);
	private final Map<Player, PlayerStatus> playerStatus;
	private int round = 1;
	public static final Logger LOG =
			Logger.getLogger(Game.class.getName());

	public Game(int width, int height, Settings settings) {
		this.field = new Field(width, height);
		this.settings = settings;
		playerStatus = new HashMap<>();
		// init player status.
		for(final Player player : Player.values()) {
			playerStatus.put(player, new PlayerStatus(player));
		}
	}
	
	public Field getField() {
		return field;
	}

	public Player getCurrentPlayer() {
		return player;
	}
	
	public PlayerStatus getPlayerStatus(Player player) {
		return playerStatus.get(player);
	}
	
	public int getRound() {
		return round;
	}
	
	public void selectMove(CellCoordinateTuple coord) {

		if (!isMoveValid(coord)) {   // sai se for inválido
			return;
		}

		field.putAtom(player, coord);
		
		fireOnMove(player, coord);
		
		field.react();
		
		moved.add(player);
		++round;
		// next player
		player = player == Player.FIRST ? Player.SECOND : Player.FIRST;
	}

	private boolean isMoveValid(CellCoordinateTuple coord) {
		// no further moves if we have a winner
		if (getWinner() != Player.NONE) {
			LOG.warning(String.format(
					"Invalid move by %s (winner %s) at (%d,%d)",
					player, getWinner(), coord.x, coord.y));
			return false;          // ou true, se optar pela lógica inversa
		}

		Player owner = field.getOwnerOfCellAtPosition(coord);

		// only allow moves into fields that are either unowned
		// or belong to the current player
		if (owner != Player.NONE && owner != player) {
			LOG.fine(String.format(
					"Invalid move by %s on opponent cell (%d,%d)",
					player, coord.x, coord.y));
			return false;
		}
		return true;
	}

	/**
	 * Determines the winner of this game.
	 *
	 * @return
	 *      The winner of the game or {@link Player#NONE} if there is no winner yet.
	 */
	public Player getWinner() {
		Player winner;

		if (moved.contains(Player.FIRST) && field.getTotalNumberOfAtomsForPlayer(Player.FIRST) == 0) {
			winner = Player.SECOND;
		} else if (moved.contains(Player.SECOND) && field.getTotalNumberOfAtomsForPlayer(Player.SECOND) == 0) {
			winner = Player.FIRST;
		} else {
			winner = Player.NONE;
		}

		return winner;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public void addMoveListener(final MoveListener l) {
		this.listeners.add(l);
	}
	
	private void fireOnMove(Player p, CellCoordinateTuple coord) {
		for(final MoveListener l : listeners) {
			l.onMove(p, coord);
		}
	}

	public void save(Path p) {
		try { FileHandler.save(p, new GameState(this)); }
		catch(IOException e){ LOG.severe(e.getMessage()); }
	}

	public static Game load(Path p, Settings s)
			throws IOException, ClassNotFoundException {
		Game g = new Game(6, 5, s);
		FileHandler.load(p).apply(g);
		return g;
	}

	public void setCurrentPlayer(Player p){ this.player = p; }
	public void setRound(int r){ this.round = r; }
}
