package org.example.repo;

import org.example.config.DatabaseConnectionConfig;
import org.example.domain.Friendship;
import org.example.domain.Tuple;
import org.example.domain.User;
import org.example.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

public class FriendshipDBRepo implements Repo<Tuple<Long,Long>, Friendship>{
    private Validator<Friendship> validator;
    public FriendshipDBRepo(Validator<Friendship> validator){this.validator=validator;}

    private PreparedStatement getStatement(String statement) throws SQLException{
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER_, DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement);
    }
    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> longLongTuple) {
        if(longLongTuple == null){
            throw new RepositoryException("id cannot be null");
        }
        String statement = "SELECT f.id1, " +
                "       f.id2, " +
                "       f.friendsfrom, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2 " +
                "FROM friendships f " +
                "LEFT JOIN users u1 ON f.id1 = u1.id " +
                "LEFT JOIN users u2 ON f.id2 = u2.id " +
                "WHERE f.id1 = ? AND f.id2 = ?;";
        try(PreparedStatement preparedStatement=getStatement(statement)){
            preparedStatement.setLong(1,longLongTuple.getLeft());
            preparedStatement.setLong(2,longLongTuple.getRight());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long user1id = resultSet.getLong("id1");
                Long user2id = resultSet.getLong("id2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                User u1 = new User(first_name_u1, last_name_u1);
                u1.setId(user1id);
                User u2 = new User(first_name_u2, last_name_u2);
                u2.setId(user2id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom);
                Tuple<Long, Long> friendId = new Tuple<>(user1id, user2id);
                friendship.setId(friendId);
                return Optional.of(friendship);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        HashSet<Friendship> set = new HashSet<>();
        String statement = "SELECT f.id1, " +
                "       f.id2, " +
                "       f.friendsfrom, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2 " +
                "FROM friendships f " +
                "LEFT JOIN users u1 ON f.id1 = u1.id " +
                "LEFT JOIN users u2 ON f.id2 = u2.id;";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long user1Id = resultSet.getLong("id1");
                Long user2Id = resultSet.getLong("id2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                User u1 = new User(first_name_u1, last_name_u1);
                u1.setId(user1Id);
                User u2 = new User(first_name_u2, last_name_u2);
                u2.setId(user2Id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom);
                Tuple<Long, Long> friendId = new Tuple<>(user1Id, user2Id);
                friendship.setId(friendId);
                set.add(friendship);
            }
            return set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "insert into friendships (id1, id2, friendsFrom) " +
                "values (?, ?, ?);";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1, entity.getId().getLeft());
            preparedStatement.setLong(2, entity.getId().getRight());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            if (preparedStatement.executeUpdate() > 0){
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) {
        if(longLongTuple == null)
            throw new RepositoryException("friendship does not exist");
        String statement = "delete from friendships where id1=? and id2=?";
        try(PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1,longLongTuple.getLeft());
            preparedStatement.setLong(2,longLongTuple.getRight());
            Optional<Friendship> deletedFriendship =  findOne(longLongTuple);
            if(preparedStatement.executeUpdate() > 0)
                return deletedFriendship;
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        if (entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "update friendships " +
                "set friendsFrom = ? " +
                "where id1 = ? AND id2 = ?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getFriendsFrom()));
            preparedStatement.setLong(2, entity.getId().getLeft());
            preparedStatement.setLong(3, entity.getId().getRight());
            if (preparedStatement.executeUpdate() > 0){
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
