package de.freewarepoint.cr;

public class FieldAnalyzer {


    /**
     * Iterates over every {@link Cell} of a {@link Field} and adds up the amount of
     * cells that are critical.
     *
     * @see #isCriticalCell(Field, CellCoordinateTuple)
     *
     * @param field The field containing the cells that will be checked.
     * @return A value determining the amount of critical cells.
     */
    public static int countAllCriticalFields(Field field) {
        int result = 0;
        for (int x = 0; x < field.getWidth(); ++x) {
            for (int y = 0; y < field.getHeight(); ++y) {
                CellCoordinateTuple coord = new CellCoordinateTuple(x, y);
                if (isCriticalCell(field, coord)) {
                    ++result;
                }
            }
        }
        return result;
    }

    /**
     * Iterates over every {@link Cell} of a {@link Field} that are owned by a given player and adds up the amount of cells that are critical.
     *
     * @see #isCriticalCell(Field, CellCoordinateTuple)
     *
     * @param field The field containing the cells that will be checked.
     * @param player The player for whom the cells will be counted.
     * @return A value determining the amount of critical cells owned by the player.
     */
    public static int countCriticalFieldsForPlayer(Field field, Player player) {
        int result = 0;
        for (int x = 0; x < field.getWidth(); ++x) {
            for (int y = 0; y < field.getHeight(); ++y) {
                CellCoordinateTuple coord = new CellCoordinateTuple(x,y);
                if (PlacementValidator.belongsToPlayer(field, player, coord) && isCriticalCell(field, coord)) {
                    ++result;
                }
            }
        }
        return result;
    }

    /**
     * Iterates over every {@link Cell} of a {@link Field} that is owned by a given player and adds up the amount of
     * cells that are endangered.
     *
     *
     * @param field The field containing the cells that will be checked.
     * @param player The current owner for whom the cells will be checked.
     * @return A value determining the amount of cells for the player, that are endangered.
     */
    public static int countEndangeredFields(Field field, Player player) {
        int endangered = 0;
        for(int x = 0; x < field.getWidth(); ++x) {
            for(int y = 0; y < field.getHeight(); ++y) {
                CellCoordinateTuple coord = new CellCoordinateTuple(x, y);
                if(PlacementValidator.isEndangered(field, player, coord)) {
                    ++endangered;
                }
            }
        }
        return endangered;
    }

    /**
     * Checks if a cell has exactly one atom less than it takes to make it react.
     *
     * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked.
     * @return <code>true</code> if the cell is critical, <code>false</code> otherwise.
     */
    public static boolean isCriticalCell(Field field, CellCoordinateTuple coord) {
        return field.isCritical(coord);
    }

    /**
     * Retrieves the amount of atoms on cells of a field that are owned by a given player.
     *
     * @param field The {@link Field} for which the atoms will be counted.
     * @param player The player whose atoms will be counted.
     * @return The amount of atoms on the field owned by the player.
     */
    public static int countOwnedAtoms(Field field, Player player) {
        return field.getTotalNumberOfAtomsForPlayer(player);
    }

    /**
     * Counts the amount of cells on a given {@link Field} that are owned by a given player.
     *
     * @param field The field that contains the cells.
     * @param player The owner for whom the cells will be counted.
     * @return The amount of cells on the field that are owned by the player.
     */
    public static int countPlayerCells(Field field, Player player) {
        return field.getPlayerFields(player);
    }

    /**
     * Checks if the cell is located on an edge or in a corner of a given field.
     *
     * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked.
     * @return <code>true</code> if the cell is located on an edge or in a corner of the field, <code>false</code> otherwise.
     */
    public static boolean isEdgeOrCornerCell(Field field, CellCoordinateTuple coord) {
        return ((coord.x == 0 || coord.x == field.getWidth() - 1) || (coord.y == 0 || coord.y == field.getHeight() - 1));
    }

    /**
     * Checks if the cell is located on an edge but not in a corner of a given field.
     *
     * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked.
     * @return <code>true</code> if the cell is located on an edge but not in a corner of the field, <code>false</code> otherwise.
     */
    public static boolean isEdgeCell(Field field, CellCoordinateTuple coord) {
        return isEdgeOrCornerCell(field, coord) && !isCornerCell(field, coord);
    }

    /**
	 * Checks if the cell is located in a corner of a given field.
	 *
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked.
	 * @return <code>true</code> if the cell is located on an edge or in the corner of the field, <code>false</code> otherwise.
	 */
	public static boolean isCornerCell(Field field, CellCoordinateTuple coord) {
		return ((coord.x == 0 || coord.x == field.getWidth() - 1) && (coord.y == 0 || coord.y == field.getHeight() - 1));
	}
}
