package de.freewarepoint.cr;

public class PlacementValidator {

    /**
     * Checks if a cell belongs to a given player and has at least one neighbour
     * cell that is a critical cell of the opposing player.
     *
     * @param field  The {@link Field} containing the {@link Cell} for which the
     *               condition will be checked.
     * @param player The owner of the cell.
     * @param coord   C
     * @return <code>true</code> if the cell belongs to the parameterized player and
     * has a critical neighbour cell owned by the opposing player,
     * <code>false</code> otherwise.
     * @see #isCriticalEnemyCell(Field, Player, CellCoordinateTuple)
     */
    public static boolean isEndangered(Field field, Player player, CellCoordinateTuple coord) {
            return belongsToPlayer(field, player, coord) && (computeDangerForCell(field, player, coord) > 0);
    }

    /**
     * Evaluates if a cell is critical and not owned by a given player.
     *
     * @param field  The {@link Field} containing the {@link Cell} for which the condition will be checked.
     * @param player The owner of the cell.
     * @param coord
     * @return <code>true</code> if the cell is critical and not owned by the parameterized player, <code>false</code> otherwise.
     */
    public static boolean isCriticalEnemyCell(Field field, Player player, CellCoordinateTuple coord) {
        return (!(belongsToPlayer(field, player, coord)) && FieldAnalyzer.isCriticalCell(field, coord));
    }

    /**
     * Counts the amount of neighbour cells that are critical but not owned by the parameterized player.
     *
     * @param field  The {@link Field} containing the {@link Cell} for which the value will be computed.
     * @param player The owner of the cell.
     * @param coord
     * @return A value between <code>0</code> and the amount of neighbours of the cell, representing the amount of critical neighbour cells owned by the opposing player.
     */
    public static int computeDangerForCell(Field field, Player player, CellCoordinateTuple coord) {
        int danger = 0;
        if (coord.x > 0 && isCriticalEnemyCell(field, player, coord)) {
            ++danger;
        }
        if( coord.x < (field.getWidth()) - 1 && isCriticalEnemyCell(field, player, coord)) {
            ++danger;
        }
        if (coord.y > 0 && isCriticalEnemyCell(field, player, coord)) {
            ++danger;
        }
        if( coord.y < (field.getHeight() - 1) && isCriticalEnemyCell(field, player, coord)) {
            ++danger;
        }
        return danger;
    }

    /**
     * Checks if a cell belongs to a given player.
     *
     * @param field  The {@link Field} containing the {@link Cell} for which the condition will be checked.
     * @param player The owner of the cell.
     * @param coord
     * @return <code>true</code> if the cell belongs to the parameterized player, <code>false</code> otherwise.
     */
    public static boolean belongsToPlayer(Field field, Player player, CellCoordinateTuple coord) {
        return field.getOwnerOfCellAtPosition(coord) == player;
    }

    /**
     * Checks if a given player can place an atom on a given cell of a given field. This is possible if it belongs to
     * the player or to nobody.
     *
     * @param field  The {@link Field} containing the {@link Cell} for which the condition will be checked.
     * @param player The player to check the condition for.
     * @param coord
     * @return <code>true</code> if the placement of an atom is possible on the given field, <code>false</code> otherwise.
     */
    public static boolean isPlacementPossible(Field field, Player player, CellCoordinateTuple coord) {
        return belongsToPlayer(field, player, coord) || field.getOwnerOfCellAtPosition(coord) == Player.NONE;
    }
}
