package com.example.companieaeriana.Domain;

import java.time.LocalDateTime;

public class TicketDTO {
    private Long ticketId;
    private Long flightId;
    private LocalDateTime purchaseTime;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private String from;
    private String to;

    public TicketDTO(Long ticketId, Long flightId, LocalDateTime purchaseTime,
                     LocalDateTime departureTime, LocalDateTime landingTime, String from,
                     String to) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.purchaseTime = purchaseTime;
        this.departureTime = departureTime;
        this.landingTime = landingTime;
        this.from = from;
        this.to = to;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
