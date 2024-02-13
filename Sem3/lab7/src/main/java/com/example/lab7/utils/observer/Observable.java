package com.example.lab7.utils.observer;

import com.example.lab7.utils.events.myEvent;

public interface Observable<E extends myEvent> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
