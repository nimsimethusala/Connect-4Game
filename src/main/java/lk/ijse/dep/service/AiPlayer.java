package lk.ijse.dep.service;

public class AiPlayer extends Player {
    public AiPlayer(Board board) {
        super(board);
    }

    @Override
    public void movePiece(int col) {
        do {
            int range = 6;
            col = (int)(Math.random()*range);

        }while (!board.isLegalMove(col));
            board.updateMove(col, Piece.GREEN);
            board.getBoardUI().update(col, false);

            if(board.findWinner().getWinningPiece()!=(Piece.EMPTY)) {
                board.getBoardUI().notifyWinner(board.findWinner());
            }else {
                if (!board.existLegalMoves())board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
            }
    }
}

