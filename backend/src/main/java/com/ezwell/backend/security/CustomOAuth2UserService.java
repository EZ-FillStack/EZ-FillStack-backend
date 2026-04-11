package com.ezwell.backend.security;

import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 1. 서비스 구분 (google, naver)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// 2. OAuth2 유저 정보 추출
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String email, name, picture, providerId;

		if ("naver".equals(registrationId)) {
			// 네이버는 response 키 안에 정보가 들어있음
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			email = (String) response.get("email");
			name = (String) response.get("name");
			picture = (String) response.get("profile_image");
			providerId = (String) response.get("id");
		} else {
			// 구글 (default)
			email = (String) attributes.get("email");
			name = (String) attributes.get("name");
			picture = (String) attributes.get("picture");
			providerId = (String) attributes.get("sub");
		}

		// 3. DB 저장 및 업데이트
		User user = saveOrUpdate(email, name, picture, registrationId, providerId);

		// 4. SuccessHandler에서 사용할 수 있도록 설정
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		return new DefaultOAuth2User(
			Collections.singleton(() -> user.getRole().name()),
			attributes,
			userNameAttributeName
		);
	}

	private User saveOrUpdate(String email, String name, String picture, String registrationId, String providerId) {
		return userRepository.findByEmail(email)
			.map(entity -> {
				// 기존 유저라면 프로필 정보 최신화
				entity.updateProfile(name, null, picture);
				return entity;
			})
			.orElseGet(() -> userRepository.save(User.builder()
				.username(registrationId + "_" + providerId)
				.email(email)
				.nickname(name)
				.provider(registrationId)
				.providerId(providerId)
				.role(Role.ROLE_USER)
				.status("ACTIVE")
				.build()));
	}
}