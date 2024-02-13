package com.example.companieaeriana.Repository;

import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.Domain.Flight;
import com.example.companieaeriana.Domain.Ticket;
import com.example.companieaeriana.config.DatabaseConnectionConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

public class FlightRepository implements Repository<Long, Flight> {

    private PreparedStatement getStatement(String statement) throws SQLException {
        //pas1: conectarea la baza de date
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER_, DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement);
    }
    @Override
    public Optional<Flight> findOne(Long aLong) {
        if(aLong == null){
            throw new RepositoryException("id must not be null");
        }
        String statement = "select * from Flights where flightId = ?";
        try(PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1,aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long flightId = resultSet.getLong("flightId");
                String flightFrom = resultSet.getString("flightFrom");
                String flightTo = resultSet.getString("flightTo");
                LocalDateTime departurTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landingTime").toLocalDateTime();
                Integer seats = resultSet.getInt("seats");
                Flight flight = new Flight(flightFrom,flightTo,departurTime,landingTime,seats);
                flight.setId(flightId);
                return Optional.of(flight);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<Flight> findAll() {
        HashSet<Flight> set = new HashSet<>();
        String statement = "select * from Flights;";
        //pas2: design & execute SQL
        try(PreparedStatement preparedStatement = getStatement(statement)){
            //pas3: process result set
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long flightId = resultSet.getLong("flightId");
                String flightFrom = resultSet.getString("flightFrom");
                String flightTo = resultSet.getString("flightTo");
                LocalDateTime departurTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landingTime").toLocalDateTime();
                Integer seats = resultSet.getInt("seats");
                Flight flight = new Flight(flightFrom,flightTo,departurTime,landingTime,seats);
                flight.setId(flightId);
                set.add(flight);
            }
            return set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Flight> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Flight> update(Flight entity) {
        if(entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        String statement = "update Flights set flightFrom = ?, flightTo = ?, departureTime = ?, landingTime = ?, seats = ? where flightId = ?";
        try(PreparedStatement preparedStatement=getStatement(statement)){
            preparedStatement.setString(1,entity.getFrom());
            preparedStatement.setString(2,entity.getTo());
            preparedStatement.setTimestamp(3,Timestamp.valueOf(entity.getDepartureTime()));
            preparedStatement.setTimestamp(4,Timestamp.valueOf(entity.getLandingTime()));
            preparedStatement.setInt(5,entity.getSeats());
            preparedStatement.setLong(6,entity.getId());
            if(preparedStatement.executeUpdate()>0){
                return Optional.empty();
            }
            return Optional.of(entity);
        }catch(SQLException e){
            throw new RepositoryException(e);
        }
    }
}
