package goldenshadow.taqminigames.enums;

public enum GameState {
    STARTING ("Starting in: "),
    RUNNING ("Time left: "),
    ENDING ("Teleporting to lobby soon!");

    private final String label;

    GameState(String label) {
        this.label = label;
    }

    public String getDescriptor() {
        return label;
    }
}
