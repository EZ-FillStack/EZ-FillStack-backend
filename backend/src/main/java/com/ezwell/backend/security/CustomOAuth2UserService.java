package com.ezwell.backend.security;

import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import com.ezwell.backend.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 구글에서 보낸 정보 추출
		String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google
		Map<String, Object> attributes = oAuth2User.getAttributes();

		String email, name, picture, providerId;

		if ("naver".equals(registrationId)) {
			// 네이버는 "response"라는 키 안에 실제 정보가 들어있음
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			email = (String) response.get("email");
			name = (String) response.get("name");
			picture = (String) response.get("profile_image");
			providerId = (String) response.get("id");
		} else if ("kakao".equals(registrationId)) {
			// 카카오는 kakao_account 안에 정보가 있고, 닉네임은 그 안의 profile 안에 있음
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

			email = (String) kakaoAccount.get("email");
			name = (String) kakaoProfile.get("nickname");
			picture = (String) kakaoProfile.get("thumbnail_image_url");
			providerId = String.valueOf(attributes.get("id")); // 카카오는 id가 Long 타입이라 문자열 변환 필요
		} else {
			email = (String) attributes.get("email");
			name = (String) attributes.get("name");
			picture = (String) attributes.get("picture");
			providerId = String.valueOf(attributes.get("sub"));
		}

		// DB에 있으면 업데이트, 없으면 신규 저장
		User user = userRepository.findByEmail(email)
			.map(entity -> {
				entity.updateProfile(name, null, picture);
				return entity;
			})
			.orElseGet(() -> User.builder()
				.username(registrationId + "_" + providerId)
				.email(email)
				.nickname(name)
				.provider(registrationId)
				.providerId(providerId)
				.role(Role.ROLE_USER)
				.build());

		userRepository.save(user);

		return new CustomUserDetails(
				user.getId(),
				user.getEmail(),
				user.getRole(),
				attributes
		);
	}
}