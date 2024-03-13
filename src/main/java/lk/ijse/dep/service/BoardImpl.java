package lk.ijse.dep.service;

import lk.ijse.dep.controller.BoardController;

public class BoardImpl implements Board {
    private Piece[][] pieces;
    private BoardUI boardUI;

    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
         pieces = new Piece[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                pieces[i][j] = Piece.EMPTY;
            }
        }
    }

    @Override
    public BoardUI getBoardUI() {
        return boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {
        for (int i = 0; i < NUM_OF_ROWS; i++){
            if (this.pieces[col][i].equals(Piece.EMPTY)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Winner findWinner() {
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length - 3; j++){
                if (pieces[i][j].equals(Piece.EMPTY)
                    && pieces[i][j].equals(Piece.GREEN)
                    && pieces[i][j+1].equals(Piece.GREEN)
                    && pieces[i][j+2].equals(Piece.GREEN)
                    && pieces[i][j+3].equals(Piece.GREEN)){
                    return new Winner(pieces[i][j],i,j,i,j+1);
                }
            }
        }

        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length - 3; j++){
                if (pieces[i][j].equals(Piece.EMPTY)
                    && pieces[i][j].equals(Piece.BLUE)
                    && pieces[i+1][j].equals(Piece.BLUE)
                    && pieces[i+2][j].equals(Piece.BLUE)
                    && pieces[i+3][j].equals(Piece.BLUE)){
                    return new Winner(pieces[i][j],i,j,i,j+1);
                }
            }
        }
        return new Winner(Piece.EMPTY);
    }

    @Override
    public boolean isLegalMove(int col) {
        if (findNextAvailableSpot(col) == -1){
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
        pieces[col][findNextAvailableSpot(col)] = move;
    }

    @Override
    public void updateMove(int col, int row, Piece move) {

    }
}
