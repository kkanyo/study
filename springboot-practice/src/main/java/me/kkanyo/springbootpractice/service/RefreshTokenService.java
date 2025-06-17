package me.kkanyo.springbootpractice.service;

import lombok.RequiredArgsConstructor;
import me.kkanyo.springbootpractice.domain.RefreshToken;
import me.kkanyo.springbootpractice.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
