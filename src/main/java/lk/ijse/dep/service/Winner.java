package lk.ijse.dep.service;

public class Winner {
    private Piece winningPiece;
    private int col1;
    private int raw1;
    private int col2;
    private int raw2;

    public Winner(Piece winningPiece) {
        this.winningPiece = winningPiece;
    }

    public Winner(Piece winningPiece, int col1, int raw1, int col2, int raw2) {
        this.winningPiece = winningPiece;
        this.col1 = col1;
        this.raw1 = raw1;
        this.col2 = col2;
        this.raw2 = raw2;
    }

    public Piece getWinningPiece() {
        return winningPiece;
    }

    public void setWinningPiece(Piece winningPiece) {
        this.winningPiece = winningPiece;
    }

    public int getCol1() {
        return col1;
    }

    public int getCol2() {
        return col2;
    }

    @Override
    public String toString() {
        return "Winner{" +
                "winningPiece=" + winningPiece +
                ", col1=" + col1 +
                ", raw1=" + raw1 +
                ", col2=" + col2 +
                ", raw2=" + raw2 +
                '}';
    }

    public int getRow2() {
        return raw2;
    }

    public int getRow1() {
        return raw1;
    }
}
