package me.kkanyo.springbootpractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kkanyo.springbootpractice.domain.Article;

@NoArgsConstructor
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    // DTO를 엔티티로 만들어주는 메서드
    // 블로그 글을 추가할 때 저장할 엔티티로 변환하는 용도로 사용
    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
