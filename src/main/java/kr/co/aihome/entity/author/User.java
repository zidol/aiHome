package kr.co.aihome.entity.author;

import kr.co.aihome.entity.common.BaseEntity;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자를 다른 계층에서 함부로 생성하지 못하도록 막는다.
@Builder
@Entity
@Table(name="spp_user")
public class User extends BaseEntity implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
    private Long userId;
	
	//계정
	@NotNull
	@Column(unique = true)
	private String username;

	//이름
	@NotNull
	@Setter
    private String name;

	@NotNull
    @Column(length = 50)
	@Setter
    private String email;

	@NotNull
    @Column(length = 255)
	@Setter
    private String password;
    
	@Setter
    private String dept;
    
    @Setter
    private String position;
    
    @Setter
    private String education;
    
    @Setter
    @Column(name = "office_num")
    private String officeNum;
    
    @NotNull
    @Setter
    private String mobile;

    @OneToMany(mappedBy = "user")
    @Setter
    private Set<Authority> authorities = new HashSet();

    @NotNull
    @Setter
    private boolean enabled;

//    @OneToMany(mappedBy = "user")
//    private List<Board> boards = new ArrayList<>();

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
