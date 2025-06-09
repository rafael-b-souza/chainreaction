package de.freewarepoint.cr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The game field, representing the board on which the single {@link Cell}s are placed.
 */
public class Field {

	private final int width, height;
	private final List<FieldListener> listeners = new ArrayList<>();
	private final List<List<Cell>> rows;

	public Field(int width, int height) {
		this.width = width;
		this.height = height;
		rows = new ArrayList<>(height);
		for (int y = 0; y < height; y++) {
			List<Cell> row = new ArrayList<>(width);
			for (int x = 0; x < width; x++) {
				row.add(x, new Cell());
			}
			rows.add(row);
		}
	}

	public Field getCopy() {
		Field copy = new Field(getWidth(), getHeight());
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				CellCoordinateTuple coord = new CellCoordinateTuple( x ,y);
				Cell orginalCell = getCellAtPosition(coord);
				Cell newCell = new Cell(orginalCell.getNumberOfAtoms(), orginalCell.getOwningPlayer());
				copy.setCellAtPosition(newCell, coord);
			}
		}
		return copy;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private Cell getCellAtPosition(CellCoordinateTuple coord) {
		List<Cell> row = rows.get(coord.y);
		return row.get(coord.x);
	}

	private void setCellAtPosition(Cell cell, CellCoordinateTuple coord) {
		List<Cell> row = rows.get(coord.y);
		row.set(coord.x, cell);
	}

	public byte getNumerOfAtomsAtPosition(CellCoordinateTuple coord) {
		return getCellAtPosition(coord).getNumberOfAtoms();
	}

	public Player getOwnerOfCellAtPosition(CellCoordinateTuple coord) {
		return getCellAtPosition(coord).getOwningPlayer();
	}

	public int getTotalNumberOfAtomsForPlayer(Player player) {
		int count = 0;

		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				CellCoordinateTuple coord = new CellCoordinateTuple( x ,y);
				if (getOwnerOfCellAtPosition(coord) == player) {
					count += getNumerOfAtomsAtPosition(coord);
				}
			}
		}

		return count;
	}
	
	public int getPlayerFields(Player player) {
		int count = 0;
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				CellCoordinateTuple coord = new CellCoordinateTuple( x ,y);
				if (getOwnerOfCellAtPosition(coord) == player) {
					++count;
				}
			}
		}

		return count;
	}
	
	/**
	 * Increases the atom count of the cell identified by the x and y coordinates by one --
	 * if the maximum size of cell hasn't been reached -- and alters the owner of the cell
	 * to the given {@link Player}, if necessary.
	 *
	 * @param player the player who puts the atom
	 * @param coord
	 */
	public void putAtom(Player player, CellCoordinateTuple coord) {
		if(getNumerOfAtomsAtPosition(coord) > 0 && !getOwnerOfCellAtPosition(coord).equals(player)) {
			throw new IllegalStateException("Not allowed to put an atom on a non empty field that is not yours");
		}
		setOwningPlayer(player, coord);
		final boolean increased = putAtomInternal(coord);
		if(increased) {
			fireOnAtomAdded(player, coord);
		}
	}

	/**
	 * Increases the atom count of the cell identified by the x and y coordinates by one.
	 *
	 * @param coord@return whether the number of atoms has been increased, or the maximal size of a cell has been reached.
	 */
	private boolean putAtomInternal(CellCoordinateTuple coord) {
		final Cell cell = getCellAtPosition(coord);
		return cell.increaseNumberOfAtoms();
	}
	
	
	public void clearCellAtPosition(CellCoordinateTuple coord) {
		final Cell cell = getCellAtPosition(coord);
		cell.clearAtoms();
		fireOnCellCleared(coord);
		setOwningPlayer(Player.NONE, coord);
	}
	
	/**
	 * Alters the owner to the given {@link Player}, if necessary.
	 *
	 * @param coord
	 */
	private void setOwningPlayer(Player player, CellCoordinateTuple coord) {
		final Cell cell = getCellAtPosition(coord);
		if(!cell.getOwningPlayer().equals(player)) {
			cell.setOwningPlayer(player);
			fireOnOwnerChange(player, coord);
		}
	}

	byte getCapacityOfCellAtPosition(CellCoordinateTuple coord) {
		int x = coord.x;
		int y = coord.y;
		byte capacity = 3;

		boolean firstColumn = (x == 0);
		boolean lastColumn = (x == (width - 1));

		if (firstColumn || lastColumn) {
			--capacity;
		}

		boolean firstRow = (y == 0);
		boolean lastRow = (y == (height - 1));

		if (firstRow || lastRow) {
			--capacity;
		}

		return capacity;
	}

	public boolean isCritical(CellCoordinateTuple coord) {
		return getNumerOfAtomsAtPosition(coord) == getCapacityOfCellAtPosition(coord);
	}

	private void spreadAtoms(CellCoordinateTuple coord) {
		int x = coord.x;
		int y = coord.y;
		final Player player = getOwnerOfCellAtPosition(coord);
		final List<Move> moves = new LinkedList<>();
		// move left
		if (x > 0) {
			moveAtom(player, moves, coord, new CellCoordinateTuple(coord.x -1, coord.y) );
		}
		// move right
		if (x < getWidth() - 1) {
			moveAtom(player, moves, coord, new CellCoordinateTuple(coord.x +1, coord.y));
		}
		// move up
		if (y > 0) {
			moveAtom(player, moves, coord, new CellCoordinateTuple(coord.x, coord.y -1));
		}
		// move down
		if (y < getHeight() - 1) {
			moveAtom(player, moves, coord, new CellCoordinateTuple(coord.x, coord.y + 1));
		}
		fireOnAtomsMoved(moves);
		// clear cell
		clearCellAtPosition(coord);
		
	}

	private void moveAtom(Player player, List<Move> moves, CellCoordinateTuple coord1, CellCoordinateTuple coord2) {
		setOwningPlayer(player, coord2);

		if (putAtomInternal(coord2)) {
			// move
			moves.add(new Move(coord1, coord2));
		}
	}

	public void react() {
		boolean stable = false;
		byte iter = 16;
		while (!stable && iter-- > 0) {
			stable = true;
			for (int x = 0; x < getWidth(); ++x) {
				for (int y = 0; y < getHeight(); ++y) {
					CellCoordinateTuple coord = new CellCoordinateTuple(x, y);
					byte count = getNumerOfAtomsAtPosition(coord);
					if (count > getCapacityOfCellAtPosition(coord)) {
						stable = false;
						spreadAtoms(coord);
					}
				}
			}
		}
	}
	
	public void addFieldListener(final FieldListener l) {
		this.listeners.add(l);
	}
	
	private void fireOnAtomAdded(Player player, CellCoordinateTuple coord) {
		for(final FieldListener l : listeners) {
			l.onAtomAdded(player, coord);
		}
	}
	
	private void fireOnAtomsMoved(List<Move> moves) {
		for(final FieldListener l : listeners) {
			l.onAtomsMoved(moves);
		}
	}
	
	private void fireOnCellCleared(CellCoordinateTuple coord) {
		for(final FieldListener l : listeners) {
			l.onCellCleared(coord);
		}
	}
	
	private void fireOnOwnerChange(Player player, CellCoordinateTuple coord) {
		for(final FieldListener l : listeners) {
			l.onOwnerChanged(player, coord);
		}
	}
}
