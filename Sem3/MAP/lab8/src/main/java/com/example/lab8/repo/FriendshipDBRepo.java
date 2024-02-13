package com.example.lab8.repo;

import com.example.lab8.config.DatabaseConnectionConfig;
import com.example.lab8.domain.FriendRequest;
import com.example.lab8.domain.Friendship;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.User;
import com.example.lab8.domain.validators.Validator;
import com.example.lab8.repo.paging.Page;
import com.example.lab8.repo.paging.Pageable;
import com.example.lab8.repo.paging.PagingRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class FriendshipDBRepo implements PagingFriendshipRepo {
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
                "       f.status, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u1.username AS username1, " +
                "       u1.password AS password1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2, " +
                "       u2.username AS username2, " +
                "       u2.password AS password2 " +
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
                FriendRequest status = FriendRequest.valueOf(resultSet.getString("status"));
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username1 = resultSet.getString("username1");
                String password1 = resultSet.getString("password1");
                String username2 = resultSet.getString("username2");
                String password2 = resultSet.getString("password2");
                User u1 = new User(username1, password1,first_name_u1, last_name_u1);
                u1.setId(user1id);
                User u2 = new User(username2, password2,first_name_u2, last_name_u2);
                u2.setId(user2id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom,status);
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
                "       f.status, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u1.username AS username1, " +
                "       u1.password AS password1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2, " +
                "       u2.username AS username2, " +
                "       u2.password AS password2 " +
                "FROM friendships f " +
                "LEFT JOIN users u1 ON f.id1 = u1.id " +
                "LEFT JOIN users u2 ON f.id2 = u2.id;";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long user1Id = resultSet.getLong("id1");
                Long user2Id = resultSet.getLong("id2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                FriendRequest status = FriendRequest.valueOf(resultSet.getString("status"));
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username1 = resultSet.getString("username1");
                String password1 = resultSet.getString("password1");
                String username2 = resultSet.getString("username2");
                String password2 = resultSet.getString("password2");
                User u1 = new User(username1,password1,first_name_u1, last_name_u1);
                u1.setId(user1Id);
                User u2 = new User(username2, password2,first_name_u2, last_name_u2);
                u2.setId(user2Id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom,status);
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
        String statement = "insert into friendships (id1, id2, friendsFrom, status) " +
                "values (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1, entity.getId().getLeft());
            preparedStatement.setLong(2, entity.getId().getRight());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            preparedStatement.setString(4,entity.getStatus().toString());
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
                "set status = ? " +
                "where id1 = ? AND id2 = ?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setString(1, entity.getStatus().toString());
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

    @Override
    public Page<Friendship> findAllOnPage(Pageable pageable){
        return findAllOnPage(pageable,null);
    }



    @Override
    public Page<Friendship> findAllOnPage(Pageable pageable, User user) {
        List<Friendship> friendships = new ArrayList<>();
        String pageSql = "SELECT f.id1, " +
                "       f.id2, " +
                "       f.friendsfrom, " +
                "       f.status, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u1.username AS username1, " +
                "       u1.password AS password1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2, " +
                "       u2.username AS username2, " +
                "       u2.password AS password2 " +
                "FROM friendships f " +
                "LEFT JOIN users u1 ON f.id1 = u1.id " +
                "LEFT JOIN users u2 ON f.id2 = u2.id" ;

        if(user != null){
            pageSql += " WHERE u1.id = " + user.getId() + " OR u2.id = " + user.getId() + " ";
        }
        pageSql += " LIMIT ? OFFSET ?";
//        SELECT COUNT(*) AS count
//        FROM friendships f
//        LEFT JOIN users u1 ON f.id1 = u1.id
//        LEFT JOIN users u2 ON f.id2 = u2.id


//        String countSql = "SELECT COUNT(*) AS count FROM friendships";
//        if(user != null){
//            countSql += " WHERE u1.id = " + user.getId() + " OR u2.id = " + user.getId() + " ";
//        }

        String countSql = "SELECT COUNT(*) AS count FROM friendships f LEFT JOIN users u1 ON f.id1 = u1.id LEFT JOIN users u2 ON f.id2 = u2.id";
        if(user != null){
            countSql += " WHERE u1.id = " + user.getId() + " OR u2.id = " + user.getId() + " ";
        }

        try(PreparedStatement pageStatement = getStatement(pageSql);
            PreparedStatement countStatement = getStatement(countSql);) {

            pageStatement.setInt(1, pageable.getPageSize());
            pageStatement.setInt(2,pageable.getPageSize()*pageable.getPageNo());
            try(ResultSet pageResultSet = pageStatement.executeQuery();
                ResultSet countResultSet = countStatement.executeQuery()){

                int count = 0;
                if(countResultSet.next()){
                    count = countResultSet.getInt("count");
                }

                while (pageResultSet.next()){
                    Long user1Id = pageResultSet.getLong("id1");
                    Long user2Id = pageResultSet.getLong("id2");
                    LocalDateTime friendsFrom = pageResultSet.getTimestamp("friendsFrom").toLocalDateTime();
                    FriendRequest status = FriendRequest.valueOf(pageResultSet.getString("status"));
                    String first_name_u1 = pageResultSet.getString("first_name_u1");
                    String last_name_u1 = pageResultSet.getString("last_name_u1");
                    String first_name_u2 = pageResultSet.getString("first_name_u2");
                    String last_name_u2 = pageResultSet.getString("last_name_u2");
                    String username1 = pageResultSet.getString("username1");
                    String password1 = pageResultSet.getString("password1");
                    String username2 = pageResultSet.getString("username2");
                    String password2 = pageResultSet.getString("password2");

                    User u1 = new User(username1,password1,first_name_u1, last_name_u1);
                    u1.setId(user1Id);
                    User u2 = new User(username2, password2,first_name_u2, last_name_u2);
                    u2.setId(user2Id);

                    Friendship friendship = new Friendship(u1, u2, friendsFrom,status);
                    Tuple<Long, Long> friendId = new Tuple<>(user1Id, user2Id);

                    friendship.setId(friendId);
                    friendships.add(friendship);
                }
                return new Page<>(friendships,count);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
