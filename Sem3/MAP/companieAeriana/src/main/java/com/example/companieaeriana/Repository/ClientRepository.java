package com.example.companieaeriana.Repository;

import com.example.companieaeriana.Domain.Client;
import com.example.companieaeriana.config.DatabaseConnectionConfig;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public class ClientRepository implements Repository<Long, Client> {
    private PreparedStatement getStatement(String statement) throws SQLException {
        //pas1: conectarea la baza de date
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER_, DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement);
    }
    @Override
    public Optional<Client> findOne(Long aLong) {
        if(aLong == null){
            throw new RepositoryException("id must not be null");
        }
        String statement = "select * from Clients where clientId = ?";
        try(PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1,aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long clientId = resultSet.getLong("clientId");
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                Client client = new Client(username,name);
                client.setId(clientId);
                return Optional.of(client);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<Client> findAll() {
        HashSet<Client> set = new HashSet<>();
        String statement = "select * from Clients;";
        //pas2: design & execute SQL
        try(PreparedStatement preparedStatement = getStatement(statement)){
            //pas3: process result set
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long clientId = resultSet.getLong("clientId");
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                Client client = new Client(username,name);
                client.setId(clientId);
                set.add(client);
            }
            return set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Client> save(Client entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) {
        return Optional.empty();
    }
}
