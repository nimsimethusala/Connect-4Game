package lk.ijse.dep.service;

import lk.ijse.dep.controller.BoardController;

public class BoardImpl implements Board {
    private final Piece[][] pieces = new Piece[NUM_OF_COLS][NUM_OF_ROWS];;
    private final BoardUI boardUI;

    public BoardImpl(BoardUI boardUI) {

        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                pieces[i][j] = Piece.EMPTY;
            }
        }
        this.boardUI = boardUI;
    }

    @Override
    public BoardUI getBoardUI() {
        return this.boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {
        for (int i = 0; i <pieces[col].length; i++) {
            if (pieces[col][i] == Piece.EMPTY) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Winner findWinner() {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j] != Piece.EMPTY) {
                    // Horizontal check
                    if (j <= pieces[i].length - 4
                        && pieces[i][j] == pieces[i][j+1]
                        && pieces[i][j] == pieces[i][j+2]
                        && pieces[i][j] == pieces[i][j+3]) {
                        return new Winner(pieces[i][j], i, j, i, j+3);
                    }
                    // Vertical check
                    if (i <= pieces.length - 4
                        && pieces[i][j] == pieces[i+1][j]
                        && pieces[i][j] == pieces[i+2][j]
                        && pieces[i][j] == pieces[i+3][j]) {
                        return new Winner(pieces[i][j], i, j, i+3, j);
                    }
                }
            }
        }
        return new Winner(Piece.EMPTY);
    }

    @Override
    public boolean isLegalMove(int col) {
        if (this.findNextAvailableSpot(col) != -1){
            return true;
        }
        return false;
    }

    @Override
    public boolean existLegalMoves() {
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                if (this.pieces[i][j].equals(Piece.EMPTY)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateMove(int col, Piece move) {
        this.pieces[col][findNextAvailableSpot(col)] = move;
    }

    @Override
    public void updateMove(int col, int row, Piece move) {

    }
}
