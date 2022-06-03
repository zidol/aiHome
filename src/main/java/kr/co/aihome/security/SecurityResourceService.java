package kr.co.aihome.security;

import kr.co.aihome.entity.author.Resources;
import kr.co.aihome.entity.author.RoleResources;
import kr.co.aihome.user.repository.ResourcesRepository;
import kr.co.aihome.user.repository.RoleResourcesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SecurityResourceService {

    
    private final RoleResourcesRepository roleResourcesRepository;

    private final ResourcesRepository resourcesRepository;


    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList(){

        // DB로부터 권한과 자원정보를 가져온다.
        LinkedHashMap<RequestMatcher,List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourcesList = resourcesRepository.findAll();  // Resources 데이터 전부 가져오기
        List<RoleResources> roleResources = roleResourcesRepository.findAll();  // RoleResources 데이터 전부 가져오기
        resourcesList.forEach(resource ->{  // For문 돌면서~
            List<ConfigAttribute> configAttributeList = new ArrayList<>();  // 권한정보 저장 리스트 생성해주고 여기에 담을꺼다.
            // 자원에 해당되는 권한 목록을 가져온다.
            Long resourceId = resource.getId();   // for문 내부에서 도는 요소. resourceId.
            roleResources.forEach(roleResource ->{  // 이중 For문 시작!
                if(roleResource.getResources().getId() == resourceId)   // Resource의 아이디와 roleResource에서 뽑은 아이디가 같다면
                    configAttributeList.add(new SecurityConfig(roleResource.getRole().getAuthority()));  // 권한정보 이름 더하기!
            });
            result.put(new AntPathRequestMatcher(resource.getResourceName(), resource.getHttpMethod()),configAttributeList);  // 해시로 {이름:[권한들]} 매핑해주기
        });
        return result;
    }
}
