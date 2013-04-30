package nyu.edu.chat;

public interface Observable {

	public void registerObserver(Observer observer, String string);
	public void removeObserver(Observer observer, String string);
	public void notifyObserver(Observer observer, String string);

}
