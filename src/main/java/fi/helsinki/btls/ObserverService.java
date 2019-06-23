package fi.helsinki.btls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.btls.datamodels.Observer;

/**
 * Handles everything related to keeping observers ready for use all the time.
 * But not persisiting them so all the data is lost after closing.
 */
public class ObserverService implements IObserverService {
    private Map<String, Observer> observers;
    private int positionsDimension;

    public ObserverService(int positionsDimension) {
        this.positionsDimension = positionsDimension;
        observers = new HashMap<>();
    }

    @Override
    public boolean addObserver(Observer observer) {
        double[] position = observer.getPosition();

        if (position.length != positionsDimension) {
            return false;
        }

        observers.put(observer.getObserverId(), observer);
        return true;
    }

    @Override
    public boolean addAllObservers(List<Observer> observers) {
        Map<String, Observer> newOnes = new HashMap<>();

        for (Observer obs : observers) {
            if (obs.getPosition().length != positionsDimension) {
                return false;
            } else {
                newOnes.put(obs.getObserverId(), obs);
            }
        }

        this.observers.putAll(newOnes);
        return true;
    }

    @Override
    public Observer deleteObserver(String observerId) {
        return observers.remove(observerId);
    }

    @Override
    public void deleteAllObservers() {
        observers.clear();
    }

    @Override
    public Observer getObserver(String observerId) {
        return observers.get(observerId);
    }

    @Override
    public List<Observer> getAllObservers() {
        return new ArrayList<>(observers.values());
    }
}
