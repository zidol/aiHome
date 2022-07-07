package kr.co.aihome.entity.author;

import kr.co.aihome.entity.common.BaseEntity;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자를 다른 계층에서 함부로 생성하지 못하도록 막는다.
@Builder
@Entity
@Table(name="user")
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
	@NotBlank
	@Column(unique = true)
	private String username;

	//이름
	@NotBlank
	@Setter
    private String name;

	@NotBlank
    @Email
    @Column(length = 50, unique = true)
	@Setter
    private String email;

	@NotBlank
    @Column(length = 255)
	@Setter
    private String password;
    
	@Setter
    @Min(value = 1)
    private int age;
    
    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Setter
    @NotNull
    @Min(value = 1)
    private Double weight;

    @OneToMany(mappedBy = "user")
    private List<Authority> authorities = new ArrayList<>();

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
