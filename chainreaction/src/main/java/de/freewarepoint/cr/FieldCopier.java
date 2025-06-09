package de.freewarepoint.cr;

public class FieldCopier {

    /**
     * Creates a copy of a given {@link Field}.
     *
     * @param field The field to be copied.
     * @return A copy of the field.
     */
    public static Field getCopyOfField(Field field) {
        return field.getCopy();
    }
}
