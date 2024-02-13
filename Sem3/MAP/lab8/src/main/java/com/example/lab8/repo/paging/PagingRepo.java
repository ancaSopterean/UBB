package com.example.lab8.repo.paging;

import com.example.lab8.domain.Entity;
import com.example.lab8.repo.Repo;
import javafx.print.PageLayout;

public interface PagingRepo<ID,E extends Entity<ID>> extends Repo<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
