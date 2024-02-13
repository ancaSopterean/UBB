package com.example.companieaeriana.Repository;

import com.example.companieaeriana.Domain.Ticket;
import com.example.companieaeriana.config.DatabaseConnectionConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

public class TicketRepository implements Repository<Long, Ticket> {

    private static PreparedStatement getStatement(String statement) throws SQLException {
        //pas1: conectarea la baza de date
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER_,DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement);
    }

    private PreparedStatement getStatement(String statement,int property) throws SQLException{
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER_,DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement,property);
    }
    @Override
    public Optional<Ticket> findOne(Long aLong) {
        if(aLong == null){
            throw new RepositoryException("id must not be null");
        }
        String statement = "select * from Tickets where ticketId = ?";
        try(PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1,aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long ticketId = resultSet.getLong("ticketId");
                Long clientId = resultSet.getLong("clientId");
                String username = resultSet.getString("username");
                Long flightId = resultSet.getLong("flightId");
                LocalDateTime purchaseTime = resultSet.getTimestamp("purchaseTime").toLocalDateTime();
                Ticket ticket = new Ticket(username,flightId,purchaseTime);
                ticket.setId(ticketId);
                return Optional.of(ticket);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<Ticket> findAll() {
        HashSet<Ticket> set = new HashSet<>();
        String statement = "select * from Tickets;";
        //pas2: design & execute SQL
        try(PreparedStatement preparedStatement = getStatement(statement)){
            //pas3: process result set
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long ticketId = resultSet.getLong("ticketId");
                Long clientId = resultSet.getLong("clientId");
                String username = resultSet.getString("username");
                Long flightId = resultSet.getLong("flightId");
                LocalDateTime purchaseTime = resultSet.getTimestamp("purchaseTime").toLocalDateTime();
                Ticket ticket = new Ticket(username,flightId,purchaseTime);
                ticket.setId(ticketId);
                set.add(ticket);
            }
            return set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Ticket> save(Ticket entity) {
        if (entity == null)
            throw new RepositoryException("entity must not be null");

        String statement = "insert into Tickets(clientId, username, flightId, purchaseTime) values (?,?,?,?);";
        try(PreparedStatement preparedStatement = getStatement(statement,Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setLong(1,entity.getClientId());
            preparedStatement.setString(2,entity.getUsername());
            preparedStatement.setLong(3,entity.getFlightId());
            preparedStatement.setTimestamp(4,Timestamp.valueOf(entity.getPurchaseTime()));
            if(preparedStatement.executeUpdate()>0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    Long userId = resultSet.getLong(1);
                    entity.setId(userId);
                }
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e){
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Ticket> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        return Optional.empty();
    }
}
