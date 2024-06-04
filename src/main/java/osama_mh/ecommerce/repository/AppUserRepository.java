package osama_mh.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import osama_mh.ecommerce.entity.AppUser;
import osama_mh.ecommerce.enums.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u  WHERE u.phoneNumber = ?1")
    Optional<AppUser> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM AppUser u WHERE u.email = ?1")
    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE u.id = ?1")
    Optional<AppUser> findById(Long id);

    @Modifying
    @Query("UPDATE AppUser u SET u.role = :userRole WHERE u.id = :userId")
    void promoteToDelivery(@Param("userId") Long userId, @Param("userRole") UserRole userRole);

    @Query("SELECT u FROM AppUser u WHERE u.phoneNumber = :phoneNumber AND u.role != 'ADMIN'")
    Optional<AppUser> findCustomerByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM AppUser u WHERE u.role = :role")
    List<AppUser> findAllByRole(@Param("role") UserRole role);

    @Modifying
    @Query("DELETE FROM AppUser u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") Long userId);

    @Query("SELECT COUNT(u)>0 FROM AppUser u WHERE u.id=:userId AND u.role=:role")
    Boolean isDeliveryExistsById(@Param("userId") Long userId, @Param("role")UserRole role);


}
