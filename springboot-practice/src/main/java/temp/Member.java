package temp;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자
@AllArgsConstructor
@Getter
@Entity // 엔티티로 지정
public class Member {
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가 (AutoIncrement)
    @Column(name = "id", updatable = false)
    private long id;    // DB 테이블의 'id' 컬럼과 매칭

    @Column(name = "name", nullable = false)
    private String name;    // DB 테이블의 'name' 컬럼과 매칭

    public void changeName(String name) {
        this.name = name;
    }
}
