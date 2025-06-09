package de.freewarepoint.cr;

public class FieldActions {

    /**
     * TODO: Javadoc - reacts after placement?
     *  @param field
     *
     * @param player
     * @param coord
     */
    public static void placeAtom(Field field, Player player, CellCoordinateTuple coord) {
        field.putAtom(player, coord);
    }

    /**
     * Causes a given {@link Field} to react, triggering every cell that reached its capacity to spread its atoms to
     * neighbour fields.
     *
     * @param field The field for which the reaction will be triggered.
     */
    public static void reactField(Field field) {
        field.react();
    }
}
