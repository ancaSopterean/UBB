package com.example.lab8.repo;

import com.example.lab8.domain.Friendship;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.User;
import com.example.lab8.repo.paging.Page;
import com.example.lab8.repo.paging.Pageable;
import com.example.lab8.repo.paging.PagingRepo;

public interface PagingFriendshipRepo extends PagingRepo<Tuple<Long,Long>, Friendship> {
    Page<Friendship> findAllOnPage(Pageable pageable, User user);

}
