package de.freewarepoint.cr;

import java.util.List;

/**
 * A listener being informed of changes to the game field like adding an atom, moving an atom and 
 * changing the owner of a cell.
 *
 * @author maik
 * @author jonny
 */
public interface FieldListener {
	
	/**
	 * An atom has been added.
	 *
	 * @param player the player who added the atom.
	 * @param coord
	 */
	public void onAtomAdded(Player player, CellCoordinateTuple coord);
	
	/**
	 * A list of atom moves occured.
	 * 
	 * @param moves
	 * 		list of atom moves.
	 */
	public void onAtomsMoved(List<Move> moves);
	
	/**
     * The owner of a field has changed.
     *
     * @param player the new player.
     * @param coord
     */
	public void onOwnerChanged(Player player, CellCoordinateTuple coord);
	
	/**
	 * A cell has been cleared.
	 *
	 * @param coord
	 */
	public void onCellCleared(CellCoordinateTuple coord);
}
