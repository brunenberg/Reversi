import java.util.ArrayList;
import java.util.Scanner;

public class Match {

	private int victoryThreshold;
	private int boardSize;
	private ArrayList<Player> players;
	private Player player1;
	private Player player2;

	public Match() {
		players = new ArrayList<Player>();
		player1 = new Player();
		player2 = new Player();
		player1.setValue(Mark.BLACK);
		player2.setValue(Mark.WHITE);
		players.add(player1);
		players.add(player2);
	}

	public void startMatch() {
		startUserInput();
		int previousStartingPlayerIndex = 1;
		boolean winnerDetermined = false;
		while (!winnerDetermined) {
			Game game = new Game(boardSize);
			int startingPlayerIndex = (previousStartingPlayerIndex + 1) % players.size();
			Player winningPlayer = game.play(players, startingPlayerIndex);
			for (Player player : players) {
				if (player == winningPlayer) {
					player.upWinAmount();
					System.out.println("De score is nu " + player1.getName() + " - " + player1.getWinAmount() + " : " + player2.getName() + " - " + player2.getWinAmount());
				}
				if (player.getWinAmount() >= victoryThreshold) {
					winnerDetermined = true;
					handleMatchEnd();
				}
			}
			if (!winnerDetermined) {
				getEnterToContinue();
			}
			previousStartingPlayerIndex = startingPlayerIndex;
		}
	}

	private void startUserInput() {
		System.out.println("==================================================");
		System.out.println("Welkom bij Reversi. Gemaakt door Wouter Brunenberg");
		victoryThreshold = victoryThresHoldInput();
		boardSize = boardSizeInput();
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setName(playerNamesInput(i));
		}
	}

	private int victoryThresHoldInput() {
		int winAmountInput = 0;
		int minimumValue = 1;
		int maximumValue = 9;
		while (true) {
			System.out.print("Aantal overwinningen die je nodig hebt om de match te winnen (1-9): ");
			Scanner scanner = new Scanner(System.in);
				if (scanner.hasNextInt()) {
					winAmountInput = (scanner.nextInt());
					if (winAmountInput < minimumValue || winAmountInput > maximumValue) {
						System.out.println("Getal moet tussen de 1 en 9 liggen.");
					}
				} else if (!scanner.hasNextInt()) {
					System.out.println("Dit is geen getal.");
				}
			if (winAmountInput >= minimumValue && winAmountInput <= maximumValue) {
				return winAmountInput;
			}
		}
	}

	private int boardSizeInput() {
		int boardSize;
		while (true) {
			System.out.print("De grootte van het bord (4/6/8/10): ");
			Scanner scanner = new Scanner(System.in);
				if (scanner.hasNextInt()) {
					boardSize = scanner.nextInt();
					if (boardSize == 4 || boardSize == 6 || boardSize == 8 || boardSize == 10) {
						return boardSize;
					} else {
						System.out.println("Deze waarde is geen mogelijkheid.");
					}
				} else if (!scanner.hasNextInt()) {
					System.out.println("Dit is geen getal.");
				}
			}
		}

	private String playerNamesInput(int currentPlayerIndex) {
		String playerName;
		while (true) {
			System.out.print("Wat is de naam van speler " + (currentPlayerIndex + 1) + ": ");
			Scanner scanner = new Scanner(System.in);
			playerName = scanner.nextLine();
			if (playerName.length() >= 1) {
				return playerName;
			}
		}
	}

	private void handleMatchEnd() {
		for (Player player : players) {
			if (player.getWinAmount() == victoryThreshold) {
				System.out.println("De winnaar is... " + player.getName() + "(" + player.getValue().getCharacter() + ")");
			}
		}
		System.out.println("Bedankt voor het spelen!");
		System.out.println("==================================================");
	}

	private void getEnterToContinue() {
		System.out.print("Klik op [ENTER] om door te gaan");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}
}
