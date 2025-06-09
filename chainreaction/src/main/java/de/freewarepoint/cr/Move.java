package de.freewarepoint.cr;


/**
 * @author jonny
 *
 */
public class Move {
	private final CellCoordinateTuple coord1, coord2;

	public Move(CellCoordinateTuple coord1, CellCoordinateTuple coord2) {
		super();
		this.coord1 = coord1;
		this.coord2 = coord2;
	}
	
	public int getX1() {
		return coord1.x;
	}
	
	public int getY1() {
		return coord1.y;
	}

	public int getX2() {
		return coord2.x;
	}
	
	public int getY2() {
		return coord2.y;
	}

	public CellCoordinateTuple getCoord1(){return coord1;}

	public CellCoordinateTuple getCoord2() {
		return coord2;
	}
}
