package me.kkanyo.springbootpractice.repository;

import me.kkanyo.springbootpractice.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {

}
