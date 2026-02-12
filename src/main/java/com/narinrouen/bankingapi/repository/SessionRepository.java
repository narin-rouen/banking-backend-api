package com.narinrouen.bankingapi.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narinrouen.bankingapi.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

	Optional<Session> findByToken(String token);

	Optional<Session> findByRefreshToken(String refreshToken);

	List<Session> findByUserIdAndIsActiveTrue(Long userId);

	@Query("SELECT s FROM Session s WHERE s.token = :token AND s.isActive = true AND s.expiresAt > :now")
	Optional<Session> findValidSessionByToken(@Param("token") String token, @Param("now") Instant now);

	@Query("SELECT s FROM Session s WHERE s.refreshToken = :refreshToken AND s.isActive = true AND s.refreshExpiresAt > :now")
	Optional<Session> findValidSessionByRefreshToken(@Param("refreshToken") String refreshToken,
			@Param("now") Instant now);

	@Modifying
	@Query("UPDATE Session s SET s.isActive = false WHERE s.user.id = :userId AND s.isActive = true")
	int deactivateAllUserSessions(@Param("userId") Long userId);

	@Modifying
	@Query("DELETE FROM Session s WHERE s.expiresAt < :now OR s.refreshExpiresAt < :now")
	int deleteExpiredSessions(@Param("now") Instant now);

	@Modifying
	@Query("UPDATE Session s SET s.isActive = false WHERE s.id = :id")
	int deactivateSession(@Param("id") Long id);
}
