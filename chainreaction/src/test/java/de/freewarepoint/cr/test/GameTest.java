package de.freewarepoint.cr.test;

import de.freewarepoint.cr.CellCoordinateTuple;
import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.Settings;
import junit.framework.Assert;
import org.junit.Test;

import java.nio.file.Path;

public class GameTest {

	@Test
	public void testCreateGame() {
		Game game = new Game(7, 8, new Settings());
		Assert.assertEquals(game.getField().getWidth(), 7);
		Assert.assertEquals(game.getField().getHeight(), 8);
		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 0);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 0);
	}

	@Test
	public void testOneMove() {
		Game game = new Game(6, 6, new Settings());
		game.selectMove(new CellCoordinateTuple(3, 3));
		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 1);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 0);
	}

	@Test
	public void testMoveToInvalidPosition() {
		Game game = new Game(6, 6, new Settings());
		game.selectMove(new CellCoordinateTuple(3, 3));
		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.selectMove(new CellCoordinateTuple(3, 3));
		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
	}

	@Test
	public void testMultipleMoves() {
		Game game = new Game(6, 6, new Settings());

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.selectMove(new CellCoordinateTuple(2, 2));

		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.selectMove(new CellCoordinateTuple(3, 2));

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.selectMove(new CellCoordinateTuple(2, 2));

		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.selectMove(new CellCoordinateTuple(5, 5));

		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 2);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.selectMove(new CellCoordinateTuple(2, 2));

		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.selectMove(new CellCoordinateTuple(3, 2));

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.selectMove(new CellCoordinateTuple(2, 2));

		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 6);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 1);
	}

	@Test
	public void testSaveAndLoad() throws Exception {
		Game g1 = new Game(6,5,new Settings());
		g1.selectMove(new CellCoordinateTuple(3,3));
		Path tmp = java.nio.file.Files.createTempFile("cr",".sav");
		g1.save(tmp);

		Game g2 = Game.load(tmp, new Settings());
		Assert.assertEquals(g1.getRound(), g2.getRound());
		Assert.assertEquals(g1.getCurrentPlayer(), g2.getCurrentPlayer());
		Assert.assertEquals(
				g1.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST),
				g2.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST));
	}
}
