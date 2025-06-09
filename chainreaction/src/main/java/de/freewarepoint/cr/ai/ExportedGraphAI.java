package de.freewarepoint.cr.ai;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import de.freewarepoint.cr.*;

/**
 * An AI that is used in the process of exporting a jABC KI Graph into a standalone AI jar. This AI is used to interact
 * with the generated graph class and executes the decision of cell values that the exported graph assigns.
 * 
 * @author Hauke Cziollek
 * @author Dennis Kuehn
 */
public abstract class ExportedGraphAI implements AI {

	private Game game;

	private EvalField evaluationResult;

	private Player player;
	
	private Field field;
	private Field fieldcopy;
	
	private int bestXCoord;
	private int bestYCoord;

	
	
	protected abstract String execute(Field fieldcopy, int x, int y, Player player);

	protected abstract Integer getResult();
	
	@Override
	public void doMove() {
		player = game.getCurrentPlayer();
		field = game.getField();
		int width = field.getWidth();
		int height = field.getHeight();
		evaluationResult = new EvalField(width, height);
		
		Exception exception = null;
		for (int i = 0; i < width*height; i++) {
			fieldcopy = FieldCopier.getCopyOfField(field);
			int x = i%width;
			int y = i/width;
			if (PlacementValidator.isPlacementPossible(fieldcopy, player, new CellCoordinateTuple(x, y) )) {

				String result = "";
				try {
					// let AI graph decide how valuable placement would be
					result = execute(fieldcopy, x, y, player);
				}
				catch (Exception e) {
					exception = e;
				}

				Integer cellValue = null;
				if (result.equals("erfolgreich")) {
					// took the branch erfolgreich, so access the calculated cell value
					cellValue = getResult();
				}

				if (cellValue == null || cellValue < 0) {
					// in case the Erfolgreich branch has not been taken or a value <0 has been assigned
					cellValue = 0;
				}

				evaluationResult.setValueAt(x, y, cellValue);
			}
			else {
				// invalid move, forbid placement
				evaluationResult.setValueAt(x, y, -1);
			}
		}
		if (exception != null) {
			exception.printStackTrace();
			showErrorMessage(exception);
		}
		chooseBestCell();
		
		game.selectMove(new CellCoordinateTuple(bestXCoord, bestYCoord));
	}
	
	private void showErrorMessage(Exception e) {
		JOptionPane.showMessageDialog(null, "This AI has thrown a "+e.getClass().getName()+" exception. It will continue to play randomly. The full stacktrace was printed to the terminal.", "Error while executing", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Iterates over all cells and picks the cell with maximal assigned cell value. If more than one such cell is found,
	 * randomly takes one of all found maximum cells.
	 */
	private void chooseBestCell() {
		List<CellCoordinateTuple> bestCells = new LinkedList<>();
		for (int x = 0; x < evaluationResult.getWidth(); x++) {
			for (int y = 0; y < evaluationResult.getHeight(); y++) {
				CellCoordinateTuple thisCell = new CellCoordinateTuple(x, y);
				
				if (bestCells.size() == 0) {
					// there is no other cell so this is the best so far
					bestCells.add(thisCell);
				} else {
					CellCoordinateTuple firstCell = bestCells.get(0);

					if (evaluationResult.getValueAt(firstCell) == evaluationResult.getValueAt(thisCell)) {
						// found another cell with as good rating
						bestCells.add(thisCell);
					}
					else if (evaluationResult.getValueAt(firstCell) < evaluationResult.getValueAt(thisCell)) {
						// found a better cell, forget every other cells
						bestCells = new LinkedList<>();
						bestCells.add(thisCell);
					}
				}
			}
		}
		Collections.shuffle(bestCells);
		bestXCoord = bestCells.get(0).x;
		bestYCoord = bestCells.get(0).y;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public String getName() {
		return "replace this string in ExportedGraphAI!";
	}
}
