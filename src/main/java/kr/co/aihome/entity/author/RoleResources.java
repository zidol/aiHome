package kr.co.aihome.entity.author;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자를 다른 계층에서 함부로 생성하지 못하도록 막는다.
public class RoleResources implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY,generator="role_resources_role_resource_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@SequenceGenerator(name = "role_resources_role_resource_id_seq", sequenceName = "role_resource_id", allocationSize = 1)
    @Column(name = "role_resource_id")
    private Long id;

    // 1:N에서 N쪽을 담당하고 있읍니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resources resources;

    // 1:N에서 N쪽을 담당하고 있읍니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority")
    private Role role;

    public void addResources(Resources resources) {
        this.resources = resources;
        resources.getRoleResources().add(this);
    }
}
