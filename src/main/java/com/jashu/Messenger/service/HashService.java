package com.jashu.Messenger.service;

import com.jashu.Messenger.util.SHA256Util;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    public String hash(String input) {
        return SHA256Util.hash(input);
    }
}
