package me.kkanyo.springbootpractice.service;

import lombok.RequiredArgsConstructor;
import me.kkanyo.springbootpractice.domain.Article;
import me.kkanyo.springbootpractice.dto.AddArticleRequest;
import me.kkanyo.springbootpractice.repository.BlogRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor    // final이 붙거나 @NotNull이 붙은 빌드의 생성자 추가
@Service    // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    // save()는 JpaRepository에서 지원하는 저장 메서드로 부모 클래스인 CrudRepository에 선언되어 있다.
    // AddArticleRequest 클래스에 저장된 값을들 article 데이터베이스에 저장한다.
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
}
