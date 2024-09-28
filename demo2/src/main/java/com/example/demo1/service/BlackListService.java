package com.example.demo1.service;

import com.example.demo1.domain.BlackList;
import com.example.demo1.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    @Transactional
    public void saveBlackList(BlackList blackList) {
        blackListRepository.save(blackList);
    }
}
