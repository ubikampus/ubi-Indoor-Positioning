package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.domain.Observer;

/**
 * Interface to abstract storing and handling of observers while application is running,
 */
public interface IObserverService {
    boolean addObserver(Observer observer);
    boolean addAllObservers(List<Observer> observers);

    Observer deleteObserver(String observerId);
    void deleteAllObservers();

    Observer getObserver(String observerId);
    List<Observer> getAllObservers();
}
