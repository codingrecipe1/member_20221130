package com.its.member.service;

import com.its.member.dto.MemberDTO;
import com.its.member.entity.MemberEntity;
import com.its.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Long save(MemberDTO memberDTO) {
//        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
//        memberRepository.save(memberEntity);

        Long savedId = memberRepository.save(MemberEntity.toSaveEntity(memberDTO)).getId();
        return savedId;
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /*
            email로 DB에서 조회를 하고
            사용자가 입력한 비번과 DB에서 조회한 비번이 일치하는지를 판단해서
            로그인 성공, 실패 여부를 리턴.
            단, email 조회결과가 없을 때도 실패
         */
    }
}
