
public class Player {

	private String name;
	private int winAmount;
	private Mark value;

	public Player() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void upWinAmount() {
		winAmount++;
	}
	
	public int getWinAmount() {
		return winAmount;
		
	}
	
	public Mark getValue() {
		return value;
	}

	public void setValue(Mark value) {
		this.value = value;
	}
}
