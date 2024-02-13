package com.example.lab8.repo;

import com.example.lab8.config.DatabaseConnectionConfig;
import com.example.lab8.domain.User;
import com.example.lab8.domain.validators.Validator;
import com.example.lab8.repo.paging.Page;
import com.example.lab8.repo.paging.Pageable;
import com.example.lab8.repo.paging.PagingRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserDBRepo implements PagingRepo<Long, User> {

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
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(username,password,firstName,lastName);
                user.setId(userId);
                user.setSalt(salt);
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
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(username,password,firstName,lastName);
                user.setId(userId);
                user.setSalt(salt);
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

        String statement = "insert into users(first_name,last_name, username, password, salt) values (?,?,?,?,?);";
        try(PreparedStatement preparedStatement = getStatement(statement,Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,entity.getFirstName());
            preparedStatement.setString(2,entity.getLastName());
            preparedStatement.setString(3,entity.getUsername());
            preparedStatement.setString(4,entity.getPassword());
            preparedStatement.setString(5,entity.getSalt());
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
        String statement = "update users set username = ?, password = ?, first_name = ?, last_name = ? where id = ?";
        try(PreparedStatement preparedStatement=getStatement(statement)){
            preparedStatement.setString(1,entity.getUsername());
            preparedStatement.setString(2,entity.getPassword());
            preparedStatement.setString(3,entity.getFirstName());
            preparedStatement.setString(4,entity.getLastName());
            preparedStatement.setLong(5,entity.getId());
            if(preparedStatement.executeUpdate()>0){
                return Optional.empty();
            }
            return Optional.of(entity);
        }catch(SQLException e){
            throw new RepositoryException(e);
        }
    }

    @Override
    public Page<User> findAllOnPage(Pageable pageable){
        List<User> users = new ArrayList<>();
        String pageSql = "select * from users limit ? offset ?";
        String countSql = "select count(*) as count from users";

        try(PreparedStatement pageStatement = getStatement(pageSql);
            PreparedStatement countStatement = getStatement(countSql);) {

            pageStatement.setInt(1,pageable.getPageSize());
            pageStatement.setInt(2,pageable.getPageSize()*pageable.getPageNo());
            try(ResultSet pageResultSet = pageStatement.executeQuery();
                ResultSet countResultSet = countStatement.executeQuery()){

                int count = 0;
                if(countResultSet.next()){
                    count = countResultSet.getInt("count");
                }

                while(pageResultSet.next()){
                    Long userId = pageResultSet.getLong("id");
                    String firstName = pageResultSet.getString("first_name");
                    String lastName = pageResultSet.getString("last_name");
                    String username = pageResultSet.getString("username");
                    String password = pageResultSet.getString("password");
                    String salt = pageResultSet.getString("salt");

                    User user = new User(username,password,firstName,lastName);
                    user.setId(userId);
                    user.setSalt(salt);

                    users.add(user);
                }
                return new Page<>(users,count);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
