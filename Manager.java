import java.io.Serializable;
import java.util.ArrayList;

public abstract class Manager implements Serializable {
  transient ArrayList<ManagerObserver> observers;

  public Manager() {
    observers = new ArrayList<>();
  }

  public void addObserver(ManagerObserver managerObserver) {
    if (observers == null) {
      observers = new ArrayList<>();
    }
    observers.add(managerObserver);
  }

  public void updateManagerObservers() {
    for (int i = 0; i < observers.size(); i++) {
      observers.get(i).updateManagerObserver();
    }
  }
}
