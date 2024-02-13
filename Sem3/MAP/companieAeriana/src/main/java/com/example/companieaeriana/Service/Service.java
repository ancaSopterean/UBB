package com.example.companieaeriana.Service;

import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Domain.Flight;
import com.example.companieaeriana.Domain.Ticket;
import com.example.companieaeriana.Domain.TicketDTO;
import com.example.companieaeriana.Repository.FlightRepository;
import com.example.companieaeriana.Repository.Repository;
import com.example.companieaeriana.Repository.TicketRepository;
import com.example.companieaeriana.utils.events.ChangeEventType;
import com.example.companieaeriana.utils.events.FlightChangeEvent;
import com.example.companieaeriana.utils.observer.Observable;
import com.example.companieaeriana.utils.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements Observable<FlightChangeEvent> {
    private Repository<Long, Client> clientRepository;
    private Repository<Long, Ticket> ticketRepository;
    private Repository<Long, Flight> flightRepository;
    private Set<Client> set;
    private List<Observer<FlightChangeEvent>> observers = new ArrayList<>();

    public Service(Repository<Long, Client> clientRepository, Repository<Long, Ticket> ticketRepository, Repository<Long, Flight> flightRepository) {
        this.clientRepository = clientRepository;
        this.ticketRepository = ticketRepository;
        this.flightRepository = flightRepository;
    }

    public Optional<Client> findOneUsername(String username){
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(),false)
                .filter(client -> client.getUsername().equals(username))
                .findFirst();
    }

    public List<TicketDTO> getClientTickets(Client client){
        Iterable<Ticket> allTickets = ticketRepository.findAll();
        List<TicketDTO> tickets = StreamSupport.stream(allTickets.spliterator(),false)
                .filter(tk -> tk.getUsername().equals(client.getUsername()))
                .map(tk -> {
                    Flight flight = flightRepository.findOne(tk.getFlightId()).get();
                    //if(tk.getUsername().equals((client.getUsername()))){

                        return new TicketDTO(tk.getId(),flight.getId(),tk.getPurchaseTime(),
                                flight.getDepartureTime(),flight.getLandingTime(),
                                flight.getFrom(), flight.getTo());
                    //}
                })
                .toList();
        return tickets;
    }

    public List<TicketDTO> getBilete24Ianuarie(){
        LocalDate date = LocalDate.of(2024, Month.JANUARY,24);
        Iterable<Ticket> allTickets = ticketRepository.findAll();
        List<TicketDTO> tickets = StreamSupport.stream(allTickets.spliterator(),false)
                .filter(tk -> {
                    Flight flight = flightRepository.findOne(tk.getFlightId()).get();
                    return flight.getDepartureTime().toLocalDate().equals(date);
                })
                .map(tk -> {
                    Flight flight = flightRepository.findOne(tk.getFlightId()).get();
                    return new TicketDTO(tk.getId(),flight.getId(),tk.getPurchaseTime(),
                                flight.getDepartureTime(),flight.getLandingTime(),
                                flight.getFrom(), flight.getTo());
                })
                .toList();
        return tickets;
    }

    public Set<String> getAllCitiesFrom(){
        Iterable<Ticket> allTickets = ticketRepository.findAll();
        Set<String> tickets = StreamSupport.stream(allTickets.spliterator(),false)
                .map(tk -> {
                    Flight flight = flightRepository.findOne(tk.getFlightId()).get();
                    //if(tickets.contains(flight.getFrom()))
                        return flight.getFrom();
                })
                .collect(Collectors.toSet());
        return tickets;
    }

    public Set<String> getAllCitiesTo(){
        Iterable<Ticket> allTickets = ticketRepository.findAll();
        Set<String> tickets = StreamSupport.stream(allTickets.spliterator(),false)
                .map(tk -> {
                    Flight flight = flightRepository.findOne(tk.getFlightId()).get();
                    //if(tickets.contains(flight.getFrom()))
                    return flight.getTo();
                })
                .collect(Collectors.toSet());
        return tickets;
    }

    public List<Flight> filtrareData(String from, String to, LocalDate departureTime){
        Iterable<Flight> allTickets = flightRepository.findAll();
        List<Flight> tickets = StreamSupport.stream(allTickets.spliterator(),false)
                .filter(tk -> {
                    Flight flight = flightRepository.findOne(tk.getId()).get();
                    return (flight.getDepartureTime().toLocalDate().equals(departureTime) &&
                        flight.getFrom().equals(from) && flight.getTo().equals(to));
                })
                .toList();
        return tickets;
    }

    public Optional<Ticket> cumparaBilet(Flight flight, Client client){
        Ticket ticket = new Ticket(client.getUsername(), flight.getId(), LocalDateTime.now());
        ticket.setClientId(client.getId());
        if(flight.getSeats() > 0) {
            Integer oldSeats = flight.getSeats();
            oldSeats -= 1;
            flight.setSeats(oldSeats);
            flightRepository.update(flight);
            notifyObservers(new FlightChangeEvent(ChangeEventType.UPDATE,null));
            return ticketRepository.save(ticket);
        }
        else
            throw new ServiceException("nu mai sunt locuri disponibile");
    }

    @Override
    public void addObserver(Observer<FlightChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FlightChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FlightChangeEvent t) {
        observers.forEach(c->c.update(t));
    }
}
