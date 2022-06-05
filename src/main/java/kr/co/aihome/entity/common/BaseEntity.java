package kr.co.aihome.entity.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * 생성자, 수정자
 */
@EntityListeners(AuditingEntityListener.class) // 자동으로 값이 들어가게 하기 위한 방법
@MappedSuperclass // BaseEntity를 상속 받은 Entity들은 아래 필드를 컬럼으로 인식
@Getter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false, length = 50)
    private String createdBy;

    @LastModifiedBy
    @Column(length = 50)
    private String updatedBy;
}
