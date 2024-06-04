package osama_mh.ecommerce.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import osama_mh.ecommerce.entity.PasswordResetToken;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    //    @Modifying
//    @Transactional
//    /// use password_reset_tokens_seq for id
//    @Query(value = "INSERT INTO password_reset_tokens (id, token, user_id, expiration)" +
//            " VALUES ( nextval('password_reset_tokens_seq'), :token, :userId, :expiration)" +
//            "ON CONFLICT (token) DO UPDATE SET expiration = EXCLUDED.expiration, user_id = EXCLUDED.user_id",
//            nativeQuery = true)
//    void upsertToken(@Param("token") String token,@Param("userId") Long userId,@Param("expiration") Instant expiration);
//
    @Query("SELECT ps FROM PasswordResetToken ps WHERE ps.user.id = ?1")
    Optional<PasswordResetToken> existsByUserId(Long userId);

    @Query("SELECT ps FROM PasswordResetToken ps JOIN FETCH ps.user WHERE ps.token = ?1 AND ps.expiration > CURRENT_TIMESTAMP")
    Optional<PasswordResetToken> findByToken(String token);

    //find by user email
    @Query("SELECT ps FROM PasswordResetToken ps JOIN FETCH ps.user WHERE ps.user.email = ?1")
    Optional<PasswordResetToken> findByUserEmail(String email);


    //update token and expire
    //@Modifying
    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetToken ps SET ps.token = :token, ps.expiration = :expiration WHERE ps.id = :id")
    void updateToken(@Param("token") String token,@Param("id") Long id,@Param("expiration") Instant expiration);

    //setIsVerified
    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetToken ps SET ps.isVerified = true WHERE ps.id = :id")
    void setIsVerified(@Param("id") Long id);

}
