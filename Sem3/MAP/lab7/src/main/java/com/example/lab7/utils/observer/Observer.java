package com.example.lab7.utils.observer;

import com.example.lab7.utils.events.myEvent;

public interface Observer<E extends myEvent>{
    void update(E e);
}
