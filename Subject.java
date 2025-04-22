//Part of the observer design pattern

public interface Subject {
    void addObserver(Observer observer);   // Method to add an observer
    void removeObserver(Observer observer); // Method to remove an observer
    void notifyObservers();  // Method to notify all observers
    void notifyWinner(String winner);  // Method to notify all observers when there's a winner
}
