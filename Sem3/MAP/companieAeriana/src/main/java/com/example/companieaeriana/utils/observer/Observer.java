package com.example.companieaeriana.utils.observer;


import com.example.companieaeriana.utils.events.myEvent;

public interface Observer<E extends myEvent>{
    void update(E e);
}
