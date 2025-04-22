// The Observer interface is designed to be a part of the Observer Design Pattern
public interface Observer {
    void update(); // When square size change due to resizing of window will update

    void updateWinner(String winner); // Notify when a winner is declared
}
