package lk.ijse.dep.service;



public class BoardImpl implements Board {

    private final BoardUI boardUI;

    public BoardImpl(BoardUI boardUI) {
        Piece[][] pieces = new Piece[Board.NUM_OF_ROWS][Board.NUM_OF_COLS];
        this.boardUI = boardUI;
    }

    @Override
    public BoardUI getBoardUI() {
        return boardUI;
    }

    @Override
    public int findNextAvailableSpot(int col) {
        return col;
    }

    @Override
    public Winner findWinner() {
        return null;
    }

    @Override
    public boolean isLegalMove(int col) {
        return false;
    }

    @Override
    public boolean existLegalMoves() {
        return false;
    }

    @Override
    public void updateMove(int col, Piece move) {

    }

    @Override
    public void updateMove(int col, int row, Piece move) {

    }
}
