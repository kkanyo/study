package me.kkanyo.springbootpractice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kkanyo.springbootpractice.domain.Article;
import me.kkanyo.springbootpractice.dto.AddArticleRequest;
import me.kkanyo.springbootpractice.dto.UpdateArticleRequest;
import me.kkanyo.springbootpractice.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor    // final이 붙거나 @NotNull이 붙은 빌드의 생성자 추가
@Service    // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    // save()는 JpaRepository에서 지원하는 저장 메서드로 부모 클래스인 CrudRepository에 선언되어 있다.
    // AddArticleRequest 클래스에 저장된 값을들 article 데이터베이스에 저장한다.
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    // 블로그 글 전체 조회
    public List<Article> findAll() {
        return blogRepository.findAll();
    }
    
    // 블로그 글 단일 조회
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    // 블로그 글 삭제
    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);

        blogRepository.delete(article);
    }

    // 블로그 글 수정
    @Transactional  // 트랜잭션 메서드
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
