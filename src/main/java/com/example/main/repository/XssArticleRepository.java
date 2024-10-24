package com.example.main.repository;

import com.example.main.entity.XssArticle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XssArticleRepository extends CrudRepository<XssArticle, Long> {

     List<XssArticle> findByArticleContainsIgnoreCase(String article);

}
