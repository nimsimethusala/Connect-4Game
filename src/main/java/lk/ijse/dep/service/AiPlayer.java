package lk.ijse.dep.service;

public class AiPlayer extends Player {
    public int contt;
    public AiPlayer(Board board) {
        super(board);
    }

    public void movePiece(int col) {
        col = colChoser();
        board.updateMove(col, Piece.GREEN);
        board.getBoardUI().update(col, false);
        Winner winner = board.findWinner();
        if (winner.getWinningPiece() != Piece.EMPTY) {
            board.getBoardUI().notifyWinner(winner);
        } else if (!board.existLegalMoves()) {
            this.board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
        }
    }

    private int colChoser() {
        int tiedColumn = 0;
        boolean UserWinningStats = false;
        for (int i = 0; i < 6; i++) {
            if (this.board.isLegalMove(i) && board.existLegalMoves()) {
                int row = this.board.findNextAvailableSpot(i);
                this.board.updateMove(i, Piece.GREEN);
                int eval = minimax(0, false);
                this.board.updateMove(i, row, Piece.EMPTY);
                if (eval == 1) {
                    contt = 0;
                    return i;
                }
                if (eval == -1) {
                    UserWinningStats = true;
                }else {
                    tiedColumn = i;
                }
            }
        }
        if ((UserWinningStats) && (this.board.isLegalMove(tiedColumn))) {
            contt = 0;
            return tiedColumn;
        }
        int col = 0;
        do {
            col = (int) (Math.random() * 6);
        }while (!this.board.isLegalMove(col));
        contt = 0;
        return col;
    }

    private int minimax(int depth,  boolean maximizingPlayer) {
        contt++;
        Winner winner = this.board.findWinner();
        if (winner.getWinningPiece() == Piece.GREEN) {
            return 1;
        }
        if (winner.getWinningPiece() == Piece.BLUE) {
            return -1;
        }
        if ((!this.board.existLegalMoves()) || (depth == 2)) {
            return 0;
        }
        if (this.board.existLegalMoves()) {
            if (maximizingPlayer) {
                for (int i = 0; i < 6; i++)
                    if (this.board.isLegalMove(i)) {
                        int row = this.board.findNextAvailableSpot(i);
                        this.board.updateMove(i, Piece.GREEN);
                        int eval = minimax(depth + 1,false);
                        this.board.updateMove(i, row, Piece.EMPTY);
                        if (eval == 1) {
                            return eval;

                        }
                    }
            } else {
                for (int i = 0; i < 6; i++) {
                    if (this.board.isLegalMove(i)) {
                        int row = this.board.findNextAvailableSpot(i);
                        this.board.updateMove(i, Piece.BLUE);
                        int eval = minimax(depth + 1, true);
                        this.board.updateMove(i, row, Piece.EMPTY);
                        if (eval == -1) {
                            return eval;
                        }
                    }
                }
            }
        }
        return 0;
    }
}