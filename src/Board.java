
public class Board {

	private Square[][] squares;
	
	public Board(int boardSize) {
		squares = new Square[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				squares[i][j] = new Square();
			}
		}
		// zet startpositie klaar
		squares[boardSize/2 - 1][boardSize/2 - 1].setValue(Mark.WHITE);
		squares[boardSize/2][boardSize/2 - 1].setValue(Mark.BLACK);
		squares[boardSize/2 - 1][boardSize/2].setValue(Mark.BLACK);
		squares[boardSize/2][boardSize/2].setValue(Mark.WHITE);
	}

	public Square[][] getSquares() {
		return squares;
	}
	
}
