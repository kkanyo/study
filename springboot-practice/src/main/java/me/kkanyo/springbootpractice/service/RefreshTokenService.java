package me.kkanyo.springbootpractice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kkanyo.springbootpractice.config.TokenProvider;
import me.kkanyo.springbootpractice.domain.RefreshToken;
import me.kkanyo.springbootpractice.repository.RefreshTokenRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    @Transactional
    public void delete() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);

        refreshTokenRepository.deleteByUserId(userId);
    }
}
