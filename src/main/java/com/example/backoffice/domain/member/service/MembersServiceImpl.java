package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.repository.MembersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{

    private final MembersRepository membersRepository;

    @Override
    @Transactional
    public MembersResponseDto.MembersCreateResponseDto signup(
            MembersResponseDto.MembersCreateResponseDto membersCreateResponseDto){
        Members member = Members.builder().build();
        membersRepository.save(member);
        return null;
    }
}
