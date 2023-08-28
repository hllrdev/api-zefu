// package com.service.zefu.security;

// import java.util.Date;
// import java.util.List;

// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.stereotype.Service;

// import com.service.zefu.enums.EnumProvider;
// import com.service.zefu.enums.EnumRole;
// import com.service.zefu.models.RoleModel;
// import com.service.zefu.models.UserModel;
// import com.service.zefu.services.RoleService;
// import com.service.zefu.services.UserService;


// @Service
// public class CustomOAuth2Service extends DefaultOAuth2UserService {
    
//     private UserService userService;
//     private RoleService roleService;

//     public CustomOAuth2Service(UserService userService, RoleService roleService) {
//         this.userService = userService;
//         this.roleService = roleService;
//     }

//     public void processOAuth2Login(String email, String name) throws OAuth2AuthenticationException {

//         boolean userExists = userService.existsByEmail(email);
//         if(!userExists){
//             UserModel userByOAuth2 = new UserModel();
//             userByOAuth2.setEmail(email);
//             userByOAuth2.setName(name);
//             userByOAuth2.setProvider(EnumProvider.GOOGLE);

//             Date now = new Date();
//             userByOAuth2.setCreatedAt(now);
//             userByOAuth2.setUpdatedAt(now);

//             RoleModel role = roleService.getRole(EnumRole.USER);
//             userByOAuth2.setRoles(List.of(role));
            
//             userService.save(userByOAuth2);
//         }
//     }
// }
