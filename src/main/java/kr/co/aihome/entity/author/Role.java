package kr.co.aihome.entity.author;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자를 다른 계층에서 함부로 생성하지 못하도록 막는다.
@Builder
@Table(name = "roles")
public class Role {

	@Id
	@Column(length = 20)
	private String authority;
	
	@Column
	private String description;
	
}