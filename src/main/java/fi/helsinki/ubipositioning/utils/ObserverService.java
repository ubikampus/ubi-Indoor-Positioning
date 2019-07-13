package fi.helsinki.ubipositioning.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.ubipositioning.datamodels.Observer;

/**
 * Handles everything related to keeping observers ready for use all the time.
 * But not persisting them so all the data is lost after closing.
 */
public class ObserverService implements IObserverService {
    private Map<String, Observer> observers;
    private int positionsDimension;

    /**
     * Initializes the service.
     *
     * @param positionsDimension Dimension in which the observers positions should be.
     */
    public ObserverService(int positionsDimension) {
        this.positionsDimension = positionsDimension;
        observers = new HashMap<>();
    }

    /**
     * Adds observer to known ones.
     *
     * @param observer New BLE listener.
     *
     * @return <code>true</code> if observers position is indicated in right dimension
     *         and otherwise <code>false</code>.
     */
    @Override
    public boolean addObserver(Observer observer) {
        double[] position = observer.getPosition();

        if (position.length != positionsDimension) {
            return false;
        }

        observers.put(observer.getObserverId(), observer);
        return true;
    }

    /**
     * Adds all observers to known ones.
     *
     * @param observers list of new BLE listeners.
     *
     * @return <code>true</code> if all the observes can be added
     *         and otherwise <code>false</code>.
     */
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

    /**
     * Delete specific observer by it's name.
     *
     * @param observerId Name of the observer.
     *
     * @return Observer that was deleted from known ones.
     */
    @Override
    public Observer deleteObserver(String observerId) {
        return observers.remove(observerId);
    }

    @Override
    public void deleteAllObservers() {
        observers.clear();
    }

    /**
     * Get observer by it's name.
     *
     * @param observerId Name of the observer.
     *
     * @return Found observer or null if doesn't exist.
     */
    @Override
    public Observer getObserver(String observerId) {
        return observers.get(observerId);
    }

    @Override
    public List<Observer> getAllObservers() {
        return new ArrayList<>(observers.values());
    }
}
