package kr.co.aihome.entity.author;

import kr.co.aihome.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString(exclude = {"roleResources"})    // toString시 roleResources은 빼고 나타내줌
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자를 다른 계층에서 함부로 생성하지 못하도록 막는다.
@AllArgsConstructor
public class Resources extends BaseEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@SequenceGenerator(name = "resources_resource_id_seq", sequenceName = "resource_id", allocationSize = 1)
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "order_num")
    private int orderNum;

    @Column(name = "resource_type")
    private String resourceType;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<RoleResources> roleResources = new HashSet<>();

}
