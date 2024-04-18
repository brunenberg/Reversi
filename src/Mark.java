
public enum Mark {
	BLACK('X'),
	WHITE('O'),
	EMPTY('.');
	
	private char character;
	
	public char getCharacter() {
		return character;
	}

	private Mark(char character) {
		this.character = character;
	}
}
