package com.example.companieaeriana.utils.observer;


import com.example.companieaeriana.utils.events.myEvent;

public interface Observable<E extends myEvent> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
