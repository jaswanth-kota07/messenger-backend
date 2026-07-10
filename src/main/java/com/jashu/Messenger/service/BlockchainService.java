package com.jashu.Messenger.service;

import com.jashu.Messenger.dto.BlockResponse;
import com.jashu.Messenger.exceptions.ResourceNotFoundException;
import com.jashu.Messenger.model.Block;
import com.jashu.Messenger.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockchainService {

    private static final String GENESIS_PREVIOUS_HASH = "0";

    private final BlockRepository blockRepository;
    private final HashService hashService;

    @Transactional
    public Block createBlock(UUID messageId, String cipherText) {
        String messageHash = hashService.hash(cipherText);
        Block previousBlock = blockRepository.findTopByOrderByBlockIndexDesc().orElse(null);

        long blockIndex = previousBlock == null ? 0 : previousBlock.getBlockIndex() + 1;
        String previousHash = previousBlock == null ? GENESIS_PREVIOUS_HASH : previousBlock.getCurrentHash();
        long timestampMillis = System.currentTimeMillis();

        Block block = new Block(
                blockIndex,
                messageId,
                messageHash,
                previousHash,
                calculateHash(blockIndex, messageId, messageHash, previousHash, timestampMillis),
                timestampMillis
        );

        return blockRepository.save(block);
    }

    public String calculateHash(long blockIndex, UUID messageId, String messageHash, String previousHash, long timestampMillis) {
        String payload = blockIndex + messageId.toString() + messageHash + previousHash + timestampMillis;
        return hashService.hash(payload);
    }

    public boolean validateChain() {
        List<Block> blocks = blockRepository.findAllByOrderByBlockIndexAsc();
        if (blocks.isEmpty()) {
            return true;
        }

        String expectedPreviousHash = GENESIS_PREVIOUS_HASH;
        for (Block block : blocks) {
            if (!expectedPreviousHash.equals(block.getPreviousHash())) {
                return false;
            }

            String calculatedHash = calculateHash(
                    block.getBlockIndex(),
                    block.getMessageId(),
                    block.getMessageHash(),
                    block.getPreviousHash(),
                    block.getTimestampMillis()
            );

            if (!calculatedHash.equals(block.getCurrentHash())) {
                return false;
            }

            expectedPreviousHash = block.getCurrentHash();
        }

        return true;
    }

    public boolean verifyMessage(UUID messageId, String cipherText) {
        Block block = blockRepository.findByMessageId(messageId).orElse(null);
        if (block == null) {
            return false;
        }

        if (!hashService.hash(cipherText).equals(block.getMessageHash())) {
            return false;
        }

        String calculatedBlockHash = calculateHash(
                block.getBlockIndex(),
                block.getMessageId(),
                block.getMessageHash(),
                block.getPreviousHash(),
                block.getTimestampMillis()
        );

        return calculatedBlockHash.equals(block.getCurrentHash());
    }

    public List<BlockResponse> getAllBlocks() {
        return blockRepository.findAllByOrderByBlockIndexAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BlockResponse getBlockByMessageId(UUID messageId) {
        return blockRepository.findByMessageId(messageId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blockchain block not found for message"));
    }

    private BlockResponse toResponse(Block block) {
        return new BlockResponse(
                block.getId(),
                block.getBlockIndex(),
                block.getMessageId(),
                block.getMessageHash(),
                block.getPreviousHash(),
                block.getCurrentHash(),
                Instant.ofEpochMilli(block.getTimestampMillis())
        );
    }
}
