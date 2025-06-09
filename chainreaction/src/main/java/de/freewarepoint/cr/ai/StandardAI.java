package de.freewarepoint.cr.ai;

import de.freewarepoint.cr.*;

import java.util.Random;

/**
 *
 * @author maik
 */
public class StandardAI implements AI {

	private Game game;
	private UtilMethods util = new UtilMethods();

	private int[] think(Field f, Player playerAI, Player playerOpposing) {
		Random r = new Random();
		int opposingAtoms = FieldAnalyzer.countOwnedAtoms(f, playerOpposing);
		int score = Integer.MIN_VALUE;
		int[] coords = new int[2];
		for(int x = 0; x < f.getWidth(); ++x) {
			for(int y = 0; y < f.getHeight(); ++y) {
				int cellvalue = calculateCellValue(f, playerAI, playerOpposing, opposingAtoms, new CellCoordinateTuple(x, y));
				if(cellvalue > score || (r.nextBoolean() && cellvalue >= score)) {
					score = cellvalue;
					coords[0] = x;
					coords[1] = y;
				}
			}
		}
		return coords;
	}
	
	private int calculateCellValue(Field f, Player playerAI, Player playerOpposing, int opposingAtoms, CellCoordinateTuple coord) {
		Player owner = f.getOwnerOfCellAtPosition(coord);
		if(owner == Player.NONE || owner == playerAI) {
			Field fieldAI = FieldCopier.getCopyOfField(f);
			FieldActions.placeAtom(fieldAI, playerAI, coord);
			FieldActions.reactField(fieldAI);
			int tmp = FieldAnalyzer.countPlayerCells(fieldAI, playerAI);
			tmp += FieldAnalyzer.countOwnedAtoms(fieldAI, playerAI);
			tmp += opposingAtoms - FieldAnalyzer.countOwnedAtoms(fieldAI, playerOpposing);
			tmp += FieldAnalyzer.isCornerCell(fieldAI, coord) ? 1 : 0;
			tmp += FieldAnalyzer.countCriticalFieldsForPlayer(fieldAI, playerAI) * 2;
			tmp -= PlacementValidator.computeDangerForCell(fieldAI, playerAI, coord) * 4;
			tmp -= FieldAnalyzer.countEndangeredFields(fieldAI, playerAI);
			return (tmp < 0 ? 0 : tmp);
		}
		return -1;
	}

	public void doMove() {
		Field field = game.getField();
		Player playerAI = game.getCurrentPlayer();
		Player playerOpposing = playerAI == Player.SECOND ? Player.FIRST : Player.SECOND;
		
		int[] coords = think(field, playerAI, playerOpposing);
		game.selectMove(new CellCoordinateTuple(coords[0], coords[1]));
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public String getName() {
		return "Standard AI";
	}
}
