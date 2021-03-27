package com.example.demo.common_part.repo;

import com.example.demo.common_part.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastAction = ?2 WHERE u.id = ?1")
    void editLastAction(Long id, Date lastAction);

    @Transactional
    @Modifying
    @Query(value = "UPDATE \"user\" SET admin_state = ?2 WHERE user_id = ?1", nativeQuery = true)
    void editAdminState(long adminId, int state);

    @Query("SELECT u FROM User u WHERE u.adminMode=true")
    List<User> findAllAdmins();

    @Transactional
    @Modifying
    @Query("DELETE  FROM User u WHERE u.chatId=?1")
    void deleteByChatId(Long chatId);
}
