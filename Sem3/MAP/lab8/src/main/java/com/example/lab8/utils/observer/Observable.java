package com.example.lab8.utils.observer;

import com.example.lab8.utils.events.myEvent;

public interface Observable<E extends myEvent> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
