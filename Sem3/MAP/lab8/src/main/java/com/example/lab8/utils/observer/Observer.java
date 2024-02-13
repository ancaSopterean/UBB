package com.example.lab8.utils.observer;

import com.example.lab8.utils.events.myEvent;

public interface Observer<E extends myEvent>{
    void update(E e);
}
