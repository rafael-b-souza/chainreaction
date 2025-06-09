// src/main/java/de/freewarepoint/cr/io/GameState.java
package de.freewarepoint.cr.io;

import de.freewarepoint.cr.*;
import java.io.Serializable;

public class GameState implements Serializable {
    private final byte[][] atoms;
    private final Player[][] owners;
    private final Player currentPlayer;
    private final int round;

    public GameState(Game game) {
        int w = game.getField().getWidth();
        int h = game.getField().getHeight();
        atoms  = new byte[w][h];
        owners = new Player[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                CellCoordinateTuple c = new CellCoordinateTuple(x, y);
                atoms[x][y]  = game.getField().getNumerOfAtomsAtPosition(c);
                owners[x][y] = game.getField().getOwnerOfCellAtPosition(c);
            }
        }
        currentPlayer = game.getCurrentPlayer();
        round         = game.getRound();
    }

    public void apply(Game game) {
        Field f = game.getField();
        for (int x = 0; x < f.getWidth(); x++) {
            for (int y = 0; y < f.getHeight(); y++) {
                CellCoordinateTuple c = new CellCoordinateTuple(x, y);
                while (f.getNumerOfAtomsAtPosition(c) > 0)
                    f.clearCellAtPosition(c);
                for (int i = 0; i < atoms[x][y]; i++)
                    f.putAtom(owners[x][y], c);
            }
        }
        game.setCurrentPlayer(currentPlayer);
        game.setRound(round);
    }
}
