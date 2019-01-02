package com.hots.springboot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hots.springboot.model.Book;

public interface BookDao extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    @Query("select b from Book b where b.bookName like %?1%")
    public List<Book> findBookByName(String bookName);

}
