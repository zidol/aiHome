package kr.co.aihome.entity.author;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자를 다른 계층에서 함부로 생성하지 못하도록 막는다.
@Builder
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {

//    public static final String ROLE_ADMIN = "ROLE_ADMIN";
//    public static final String ROLE_USER = "ROLE_USER";
//    public static final String ROLE_STUDENT = "ROLE_STUDENT";
//
//    public static final Authority ADMIN_AUTHORITY = Authority.builder().authority(ROLE_ADMIN).build();
//    public static final Authority USER_AUTHORITY = Authority.builder().authority(ROLE_USER).build();
//    public static final Authority STUDENT_AUTHORITY = Authority.builder().authority(ROLE_STUDENT).build();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority")
    private Role authority;
    
    public void addUser(User user) {
    	this.user = user;
    	user.getAuthorities().add(this);
    }
    
    @Override
    public String getAuthority() {
        return authority.getAuthority().toString();
    }
    
//    public Role getRole() {
//        return authority;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority)) return false;
        Authority authority1 = (Authority) o;
        return Objects.equals(authority, authority1.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }

}
