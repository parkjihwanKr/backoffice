package com.example.backoffice.domain.reaction.service;

import com.example.backoffice.domain.reaction.repository.ReactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionsServiceImpl implements ReactionsService{

    private final ReactionsRepository reactionsRepository;

}
