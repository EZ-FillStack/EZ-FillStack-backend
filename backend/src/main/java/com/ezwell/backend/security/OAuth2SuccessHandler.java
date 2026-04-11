package com.ezwell.backend.security;

import com.ezwell.backend.domain.user.User;
import com.ezwell.backend.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler; // 추가됨
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
																			Authentication authentication) throws IOException {

		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

		String email = (String) oAuth2User.getAttributes().get("email");

		if (email == null && oAuth2User.getAttributes().containsKey("response")) {
			java.util.Map<String, Object> responseMap = (java.util.Map<String, Object>) oAuth2User.getAttributes().get("response");
			email = (String) responseMap.get("email");
		}

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 유저입니다."));

		String token = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());

		String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/login-success")
			.queryParam("token", token)
			.build().toUriString();

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}