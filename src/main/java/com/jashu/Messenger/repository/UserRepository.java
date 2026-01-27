package com.jashu.Messenger.repository;
import com.jashu.Messenger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public boolean existsByUser(String username);

    public User getDetailsByUser(String user);
    public User findByUser(String user);
}
