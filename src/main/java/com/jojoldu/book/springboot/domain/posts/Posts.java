package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
/*
 - 테이블과 링크될 클래스임을 나타냅니다.
 - 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭합니다.
 ex) SalesManager.java -> sales_manager table
 */
public class Posts extends BaseTimeEntity {

    @Id
    /*
      - 해당 테이블의 PK 필드를 나타냅니다.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*
      - PK의 생성규칙을 나타냅니다.
     */
    private Long id;

    @Column(length = 500, nullable = false)
    /*
      - 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모드 칼럼이 됩니다.
      - 사용하는 이유는, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용합니다.
     */
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title,String content,String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title,String content) {
        this.title = title;
        this.content = content;
    }

}
