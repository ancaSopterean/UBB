package com.example.lab7.repo;

import com.example.lab7.config.DatabaseConnectionConfig;
import com.example.lab7.domain.User;
import com.example.lab7.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public class UserDBRepo implements Repo<Long, User> {

    private Validator<User> validator;
    public UserDBRepo(Validator<User> validator){
        this.validator = validator;
    }

    private PreparedStatement getStatement(String statement) throws SQLException{
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
    public Optional<User> findOne(Long aLong) {
        if(aLong == null){
            throw new RepositoryException("id must not be null");
        }
        String statement = "select * from users where id = ?";
        try(PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1,aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User(firstName,lastName);
                user.setId(userId);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }


    @Override
    public Iterable<User> findAll() {
        HashSet<User> set = new HashSet<>();
        String statement = "select * from users;";
        //pas2: design & execute SQL
        try(PreparedStatement preparedStatement = getStatement(statement)){
            //pas3: process result set
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User(firstName,lastName);
                user.setId(userId);
                set.add(user);
            }
            return set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        if (entity == null)
            throw new RepositoryException("entity must not be null");
        validator.validate(entity);

        String statement = "insert into users(first_name,last_name) values (?,?);";
        try(PreparedStatement preparedStatement = getStatement(statement,Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getFirstName());
            preparedStatement.setString(2,entity.getLastName());
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
    public Optional<User> delete(Long aLong) {
        if(aLong == null)
            throw new RepositoryException("user does nto exist");
        String statement = "delete from users where id=?";
        try(PreparedStatement preparedStatement=getStatement(statement)){
            preparedStatement.setLong(1,aLong);
            Optional<User> deletedUser = findOne(aLong);
            if(preparedStatement.executeUpdate() > 0){
                return deletedUser;
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

    }

    @Override
    public Optional<User> update(User entity) {
        if(entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "update users set first_name = ?, last_name = ? where id = ?";
        try(PreparedStatement preparedStatement=getStatement(statement)){
            preparedStatement.setString(1,entity.getFirstName());
            preparedStatement.setString(2,entity.getLastName());
            preparedStatement.setLong(3,entity.getId());
            if(preparedStatement.executeUpdate()>0){
                return Optional.empty();
            }
            return Optional.of(entity);
        }catch(SQLException e){
            throw new RepositoryException(e);
        }

    }
}
