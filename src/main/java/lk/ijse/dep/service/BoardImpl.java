package lk.ijse.dep.service;

import lk.ijse.dep.controller.BoardController;

public class BoardImpl implements Board {
    public Piece[][] pieces = new Piece[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];
    private BoardUI boardUI;

    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
    }

    public BoardImpl(BoardController boardController){
        this.boardUI = boardController;
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
        return null;
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

    }

    @Override
    public void updateMove(int col, int row, Piece move) {

    }
}
