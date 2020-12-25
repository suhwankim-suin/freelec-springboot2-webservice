package com.jojoldu.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
/*
    - JPA Entity 클래스들이 BaseTimeEntity 를 상속할 경우 필드들(createdDate,modifiedDate)도
      컬럼으로 인식하도록 합니다.
 */
@MappedSuperclass
/*
    - BaseTimeEntity 클래스에 Auditing 기능을 포함시킴니다.
 */
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /*
        - Entity가 생성되어 저장될때 시간이 자동 저장됩니다.
     */
    @CreatedDate
    private LocalDateTime createdDate;

    /*
        - 조회한 Entity의 값을 변경할때 시간이 자동 저장됩니다.
     */
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
