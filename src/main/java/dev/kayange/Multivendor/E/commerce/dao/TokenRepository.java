package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenByToken(String token);

    @Query("""
        SELECT token
        FROM Token token
        WHERE token.user.id = :id
    """)
    Optional<Token> findTokenByUserId(Long id);
}
