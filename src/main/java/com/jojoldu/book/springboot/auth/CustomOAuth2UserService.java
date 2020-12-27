package com.jojoldu.book.springboot.auth;

import com.jojoldu.book.springboot.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        /*
         - registrationId
         - 현재로그인 진행중인 서비스를 구분하는 코드입니다.
         - 지금은 구글만 사용하는 불필요한 값이지만 이후 네이버로그인 연동시에 네이버로그인이지,구글 로그인인지
           구분하기 위해 사용합니다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        /*
         - userNameAttributeName
         - OAuth2 로그인 진행시 key가 되는 필드값을 이야기 합니다. Primary Key 와 같은 의미입니다.
         - 구글의 경우 기본적으로 코드를 지원하지만,네이버 카카오 등은 기본지원하지 않습니다. 구글의 기본코드는 "sub"입니다.
         - 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용됩니다.
         */
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        /*
         - OAuthAttributes
         - OAuth2UserService 를 통해 가져온 OAuth2User의 attribure 를 담을 클래스입니다.
         - 이후 네이버등 다른 소셜로그인도 이 클래스를 사용합니다.
         - 바로 아래에서 이 클래스의 코드가 나오니 차례로 생성하시면 됩니다.
         */
        OAuthAttributes attributes = OAuthAttributes.of(registrationId,userNameAttributeName,
                oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        /*
         - SessionUser
         - 세션에 사용자 정보를 저장하기 위한 Dto 클래스 입니다.
         */
        httpSession.setAttribute("user",new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new
                        SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());


    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
