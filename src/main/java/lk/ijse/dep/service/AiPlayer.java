package lk.ijse.dep.service;
import java.util.ArrayList;

public class AiPlayer extends Player{
    private static int blue;
    private static Board board;
    public AiPlayer(Board board){
        super(board);
        this.board = board;
    }

    @Override
    public void movePiece(int col) {
        Board currentState = getCurrentState(new BoardImpl(board.getBoardUI()));
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();
        int bestMoveToWin = mcts.bestMove();
        int bestMoveToDefend = checkMove(currentState);

        if (mcts.finalMove == true){
            col = bestMoveToWin;
        }
        else {
            if (bestMoveToDefend == -1){
                col = bestMoveToWin;
            }
            else col = bestMoveToDefend;
        }

        board.updateMove(col, Piece.GREEN);
        board.getBoardUI().update(col, false);

        Winner winner = board.findWinner();
        if (winner.getWinningPiece() != Piece.EMPTY) {
            board.getBoardUI().notifyWinner(winner);

        } else if (!board.existLegalMoves()){
            board.getBoardUI().notifyWinner(new Winner(Piece.EMPTY));
        }
    }

    private int checkMove(Board currentState) {
        // Check vertical combinations
        for (int i = 0; i < Board.NUM_OF_COLS; i++) {
            int row = board.findNextAvailableSpot(i);
            if (row < 3 || row == -1){
                continue;
            }
            int j = row-1;
            while(j > row-4 && currentState.getPiece()[i][j] == Piece.BLUE) {
                blue++;
                j--;
            }
            if (blue == 3) {
                return i;
            }
        }

        // Check horizontal combination
        for (int i = 0; i < Board.NUM_OF_COLS; i++) {
            int row = board.findNextAvailableSpot(i);
            if (row == -1) {
                continue;
            }
            for (int j = i - 3; j <= i; j++) {
                if (j < 0) {
                    continue;
                }

                for (int k = j; k < j + 4; k++) {
                    if (k == i || k >= Board.NUM_OF_COLS) {
                        continue;
                    }
                    if (currentState.getPiece()[k][row] == Piece.BLUE) {
                        blue++;
                    }
                    else break;
                }
                if (blue == 3) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Piece[][] getCurrentStatePiece() {
        Piece[][] currentStatePiece = new Piece[Board.NUM_OF_COLS][Board.NUM_OF_ROWS];
        Piece[][] boardPiece = board.getPiece();

        for (int i = 0; i < Board.NUM_OF_COLS; i++) {
            for (int j = 0; j < Board.NUM_OF_ROWS; j++) {
                currentStatePiece[i][j] = boardPiece[i][j];
            }
        }
        return currentStatePiece;
    }

    public static BoardImpl getCurrentState(BoardImpl logicalBoard) {
        Piece[][] currentStatePiece = getCurrentStatePiece();
        for (int i = 0; i < Board.NUM_OF_ROWS; i++) {
            for (int j = 0; j < Board.NUM_OF_COLS; j++) {
                logicalBoard.updateMove(j, i, currentStatePiece[j][i]);
            }
        }
        return logicalBoard;
    }

    private static class Node {
        private int col;
        private Node parent = null;
        private int visits = 0;
        private int wins = 0;
        private int moves = 0;
        private double ucbValue = 0;
        private ArrayList<Node> childArray = new ArrayList<>();

        private Node() {}
        private Node(int col, Node parent) {
            this.col = col;
            this.parent = parent;
        }
    }

    private static class MonteCarloTreeSearch {
        Node rootNode;
        ArrayList<Node> maxUcbNodes;
        boolean finalMove = false;

        MonteCarloTreeSearch() {}

        private int bestMove() {
            rootNode = new Node();
            for (int i = 0; i < 6; i++) {
                select(rootNode);
            }
            if (maxUcbNodes == null || maxUcbNodes.size() == 0) {
                bestMove();
            }
            int bestMove;
            do {
                bestMove = (int) (Math.random() * maxUcbNodes.size());
            }while (bestMove == maxUcbNodes.size());
            return maxUcbNodes.get(bestMove).col;
        }

        private void select(Node parentNode) {
            ArrayList<Node> childList = parentNode.childArray;
            if (childList.size() > 0) {
                for (Node child : childList) {
                    if (child.visits == 0) {
                        rollOut(child);
                        return;
                    }
                }
                Node tempMaxUcbNode = childList.get(0);
                for (Node child : childList) {
                    if (child.ucbValue > tempMaxUcbNode.ucbValue) {
                        tempMaxUcbNode = child;
                    }
                }
                if (tempMaxUcbNode.childArray.size() == 0) {
                    expand(tempMaxUcbNode);
                }
                else select(tempMaxUcbNode);

            } else expand(parentNode);
        }

        private void expand(Node parentNode) {
            for (int i = 0; i < 6; i++) {
                parentNode.childArray.add(new Node(i, parentNode));
            }
            rollOut(parentNode.childArray.get(0));
        }

        private void rollOut(Node nonVisitedNode) {
            Board logicalBoard = AiPlayer.getCurrentState(new BoardImpl(board.getBoardUI()));

            int wins = 0;
            int moves = 0;
            Piece currentPlayer = Piece.BLUE;
            Piece winningPiece;

            boolean firstTime = true;
            while ((winningPiece = logicalBoard.findWinner().getWinningPiece()) == Piece.EMPTY
                    && logicalBoard.existLegalMoves()) {
                currentPlayer = (currentPlayer == Piece.GREEN) ? Piece.BLUE : Piece.GREEN;  // Switch to the other player
                int randomMove;
                do{
                    if (firstTime) {
                        randomMove = nonVisitedNode.col;
                        firstTime = false;
                    } else {
                        randomMove = (int) (Math.random() * 6);
                    }
                } while(randomMove == 6 || !logicalBoard.isLegalMove(randomMove));

                logicalBoard.updateMove(randomMove, currentPlayer);  // Update with the current player's piece
                if (currentPlayer == Piece.GREEN) {
                    moves++;
                }

            }
            if (winningPiece == Piece.GREEN) {
                wins = 2;
            }
            else if (winningPiece == Piece.BLUE) {
                wins = 0;
                moves = 0;
            }
            else wins = 1;

            if (winningPiece == Piece.GREEN && moves == 1) {
                this.finalMove = true;
            }
            backPropagate(nonVisitedNode, wins, moves);
        }

        private void backPropagate(Node rolledOutNode, int win, int moves) {
            Node traversingNode = rolledOutNode;
            traversingNode.wins += win;
            traversingNode.moves += moves;
            traversingNode.visits ++;

            while(traversingNode.parent != null) {
                Node parentTraversingNode = traversingNode.parent;
                parentTraversingNode.wins += win;
                parentTraversingNode.moves += moves;
                parentTraversingNode.visits ++;
                traversingNode = traversingNode.parent;
            }
            maxUcbNodes = null;
            updateMaxUcbNode(rootNode);
        }

        private void updateMaxUcbNode(Node node) {
            if (node == rootNode && node.childArray.size() == 0) {
                return;
            }
            for (Node child : node.childArray) {
                if (child.visits > 0) {
                    child.ucbValue = child.wins/(child.visits * 1.0) + Math.sqrt(2) * Math.sqrt(Math.log(child.parent.visits) /child.visits);

                    if (board.isLegalMove(child.col) && child.ucbValue > 0) {
                        if (maxUcbNodes == null || child.ucbValue > maxUcbNodes.get(0).ucbValue || (child.ucbValue == maxUcbNodes.get(0).ucbValue && child.moves < maxUcbNodes.get(0).moves)) {
                            maxUcbNodes = new ArrayList<>();
                            maxUcbNodes.add(child);
                        }
                        else if (child.ucbValue == maxUcbNodes.get(0).ucbValue && child.moves == maxUcbNodes.get(0).moves) {
                            maxUcbNodes.add(child);
                        }
                    }
                    if (child.childArray.size() != 0) {
                        updateMaxUcbNode(child);
                    }
                }
            }
        }
    }
}