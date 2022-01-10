package flashcards;

public class Card {
    private String front;
    private String back;

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public Card() {
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }
}
