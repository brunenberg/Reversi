import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {

	private Board board;

	public Game(int boardSize) {
		board = new Board(boardSize);
	}

	public Player play(ArrayList<Player> players, int startingPlayerIndex) {
		String validPattern = generateValidPattern();
		int currentPlayerIndex = startingPlayerIndex;
		while (true) {
			Player currentPlayer = players.get(currentPlayerIndex);
			Player otherPlayer = players.get((currentPlayerIndex + 1) % 2);
			ArrayList<Square> possibleSquares = getPossibleSquares(currentPlayer, otherPlayer);
			if (possibleSquares.size() > 0) {
				printBoard();
				playMove(currentPlayer, otherPlayer, possibleSquares, validPattern);
			} else if (possibleSquares.size() < 1) {
				possibleSquares = getPossibleSquares(otherPlayer, currentPlayer);
				if (possibleSquares.size() <= 0) {
					Player winningPlayer = handleGameEnd(players);
					return winningPlayer;
				} else if (possibleSquares.size() > 0) {
					System.out.println("Er zijn geen zetten mogelijk.");
				}
			}
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		}
	}
	
	private String generateValidPattern() {
		// genereer een patroon voor het checken van geldige input
		String validPattern = "";
		if (board.getSquares().length < 10) {
			validPattern = "^([a-" + (char) ('a' + board.getSquares().length - 1) + "]|[A-" + (char) ('A' + board.getSquares().length - 1) + "])[1-" + board.getSquares().length + "]$";
		} else if (board.getSquares().length >= 10) {
			validPattern = "^([a-" + (char) ('a' + board.getSquares().length - 1) + "]|[A-" + (char) ('A' + board.getSquares().length - 1) + "])([1-9]|" + board.getSquares().length + ")$";
		}
		return validPattern;
	}

	private void printBoard() {
		// print het bord
		printTopBottomRow();
		printLetterRow();
		printBoardContent();
		printLetterRow();
		printTopBottomRow();
	}

	private void printTopBottomRow() {
		// print de bovenste of onderste rij van het bord
		System.out.print("+");
		for (int i = 0; i < board.getSquares().length * 2 + 5; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}

	private void printLetterRow() {
		// print de rij met letters (tweede en een-na-laatste rij)
		System.out.print("|   ");
		for (int i = 0; i < board.getSquares().length; i++) {
			System.out.print((char) ('a' + i) + " ");
		}
		System.out.println("  |");
	}

	private void printBoardContent() {
		// print elke rij met de waardes van de stenen van het bord
		for (int i = 0; i < board.getSquares().length; i++) {
			System.out.print("|");
			if (i < 9) {
				System.out.print(" ");
			}
			System.out.print((i + 1) + " ");
			for (int j = 0; j < board.getSquares().length; j++) {
				System.out.print(board.getSquares()[i][j].getValue().getCharacter() + " ");
			}
			System.out.print(i + 1);
			if (i < 9) {
				System.out.print(" ");
			}
			System.out.println("|");
		}
	}

	private void playMove(Player currentPlayer, Player otherPlayer, ArrayList<Square> possibleSquares,
			String validPattern) {
		boolean validInput = false;
		while (validInput == false) {
			System.out.print(currentPlayer.getName() + "(" + currentPlayer.getValue().getCharacter() + ") voer je beurt in: ");
			Scanner scanner = new Scanner(System.in);
			// kijk of de invoer een vakje op het bord is
			if (scanner.hasNext(validPattern)) {
				String moveInput = scanner.next(validPattern).toLowerCase();
				int x = Integer.parseInt(moveInput.substring(1)) - 1;
				int y = moveInput.charAt(0) - 'a';
				// kijk of het vakje leeg is
				if (board.getSquares()[x][y].getValue() == Mark.EMPTY) {
					// kijk of het een legale zet is
					for (Square possibleSquare : possibleSquares) {
						if (possibleSquare == board.getSquares()[x][y]) {
							validInput = true;
						}
					}
					if (validInput == true) {
						placeDisks(currentPlayer, otherPlayer, x, y);
					} else if (validInput == false) {
						System.out.println("Deze zet is niet legaal");
					}
				} else {
					System.out.println("Dit vakje is niet leeg.");
				}
			} else {
				System.out.println("Dit vakje bestaat niet.");
			}
		}
	}

	private void placeDisks(Player currentPlayer, Player otherPlayer, int x, int y) {
		board.getSquares()[x][y].setValue(currentPlayer.getValue());
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int row = x + i;
				int column = y + j;
				// kijk of er een rij van stenen is van de tegenstander waarvan de waarde veranderd moet worden
				if (row >= 0 && row < board.getSquares().length && column >= 0 && column < board.getSquares().length
						&& !(i == 0 && j == 0) // Skip the current square
						&& board.getSquares()[row][column].getValue() == otherPlayer.getValue()) {
					row += i;
					column += j;
					while (row >= 0 && row < board.getSquares().length && column >= 0
							&& column < board.getSquares().length
							&& board.getSquares()[row][column].getValue() == otherPlayer.getValue()) {
						row += i;
						column += j;
					}
					// verander de waarde van de rij van stenen van de tegenstander
					if (row >= 0 && row < board.getSquares().length && column >= 0 && column < board.getSquares().length
							&& board.getSquares()[row][column].getValue() == currentPlayer.getValue()) {
						row -= i;
						column -= j;
						while (row != x || column != y) {
							board.getSquares()[row][column].setValue(currentPlayer.getValue());
							row -= i;
							column -= j;
						}
					}
				}
			}
		}
	}

	public ArrayList<Square> getPossibleSquares(Player currentPlayer, Player otherPlayer) {
		ArrayList<Square> possibleSquares = new ArrayList<Square>();
		for (int i = 0; i < board.getSquares().length; i++) {
			for (int j = 0; j < board.getSquares().length; j++) {
				if (board.getSquares()[i][j].getValue() == Mark.EMPTY) {
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							if (!(x == 0 && y == 0)) {
								int row = x + i;
								int column = y + j;
								// kijk of de steen naast de laatst gecheckte steen van de tegenstander is
								if (row >= 0 && row < board.getSquares().length && column >= 0
										&& column < board.getSquares().length
										&& board.getSquares()[row][column].getValue() == otherPlayer.getValue()) {
									// ga in dezelfde richting totdat er een leeg vakje is of een vakje dat van de tegenstander is
									while (row >= 0 && row < board.getSquares().length && column >= 0
											&& column < board.getSquares().length
											&& board.getSquares()[row][column].getValue() == otherPlayer.getValue()) {
										row += x;
										column += y;
									}
									// als een steen van de huidige speler is gevonden, voeg de zet toe aan de arraylist met mogelijke zetten
									if (row >= 0 && row < board.getSquares().length && column >= 0
											&& column < board.getSquares().length
											&& board.getSquares()[row][column].getValue() == currentPlayer.getValue()) {
										possibleSquares.add(board.getSquares()[i][j]);
									}
								}
							}
						}
					}
				}
			}
		}
		return possibleSquares;
	}

	private Player handleGameEnd(ArrayList<Player> players) {
		printBoard();
		ArrayList<Integer> playerScores = new ArrayList<Integer>();
		for (int i = 0; i < players.size(); i++) {
			playerScores.add(0);
		}
		for (int i = 0; i < board.getSquares().length; i++) {
			for (int j = 0; j < board.getSquares().length; j++) {
				for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
					if (players.get(playerIndex).getValue() == board.getSquares()[i][j].getValue()) {
						playerScores.set(playerIndex, playerScores.get(playerIndex) + 1);
					}
				}
			}
		}
		int maxScore = Collections.max(playerScores);
		int minScore = Collections.min(playerScores);
		if (maxScore == minScore) {
			return null;
		}
		int winningPlayerIndex = playerScores.indexOf(maxScore);
		Player winningPlayer = players.get(winningPlayerIndex);
		if (winningPlayer != null) {
			System.out.println(winningPlayer.getName() + "(" + winningPlayer.getValue().getCharacter() + ") heeft gewonnen met " + maxScore + "-" + minScore);
		}
		return winningPlayer;
	}

}
