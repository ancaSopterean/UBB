package com.example.companieaeriana.utils.events;

import com.example.companieaeriana.Domain.Flight;

public class FlightChangeEvent implements  myEvent{
    private ChangeEventType type;
    private Flight data, oldData;

    public FlightChangeEvent(ChangeEventType type, Flight data) {
        this.type = type;
        this.data = data;
    }

    public FlightChangeEvent(ChangeEventType type, Flight data, Flight oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Flight getData() {
        return data;
    }

    public Flight getOldData() {
        return oldData;
    }
}
