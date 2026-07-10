package com.jashu.Messenger.repository;

import com.jashu.Messenger.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockRepository extends JpaRepository<Block, UUID> {

    Optional<Block> findTopByOrderByBlockIndexDesc();

    List<Block> findAllByOrderByBlockIndexAsc();

    Optional<Block> findByMessageId(UUID messageId);
}
