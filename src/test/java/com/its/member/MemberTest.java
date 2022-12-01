package com.its.member;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberService memberService;

    // 회원가입 테스트
    // 신규회원 데이터 생성(DTO)
    // 회원가입 기능 실행
    // 가입 성공 후 가져온 id 값으로 DB 조회를 하고
    // 가입시 사용한 email과 DB에서 조회한 email이 일치하는지를 판단
    @Test
    @Transactional
    @Rollback(value = true)
    public void memberSaveTest() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("testEmail5");
        memberDTO.setMemberPassword("testPassword");
        memberDTO.setMemberName("testName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");
        Long saveId = memberService.save(memberDTO);
        MemberDTO savedMember = memberService.findById(saveId);
        assertThat(memberDTO.getMemberEmail()).isEqualTo(savedMember.getMemberEmail());
//        assertThat(memberDTO.getMemberEmail()).isEqualTo("tttttt");
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인 테스트")
    public void loginTest() {
        String loginEmail = "loginEmail";
        String loginPassword = "loginPassword";
        // 1. 회원가입
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail(loginEmail);
        memberDTO.setMemberPassword(loginPassword);
        memberDTO.setMemberName("loginName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");
        memberService.save(memberDTO);
        // 2. 로그인 수행
        MemberDTO loginDTO = new MemberDTO();
        loginDTO.setMemberEmail(loginEmail);
        loginDTO.setMemberPassword(loginPassword);
        MemberDTO loginResult = memberService.login(loginDTO);
        // 3. 로그인 결과가 null 이 아니면 테스트 통과
        assertThat(loginResult).isNotNull();
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 수정 테스트")
    public void updateTest() {
        MemberDTO memberDTO = newMember();
        Long savedId = memberService.save(memberDTO);

        // 수정용 MemberDTO
        memberDTO.setId(savedId);
        memberDTO.setMemberName("수정 이름");

        // 수정처리
        memberService.update(memberDTO);

        // DB에서 조회한 이름이 수정할 때 사용한 이름과 같은지 확인
        MemberDTO memberDB = memberService.findById(savedId);
        assertThat(memberDB.getMemberName()).isEqualTo(memberDTO.getMemberName());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원 삭제 테스트")
    public void deleteTest() {
        MemberDTO memberDTO = newMember();
        Long savedId = memberService.save(memberDTO);
        memberService.delete(savedId);
        assertThat(memberService.findById(savedId)).isNull();
    }

    public MemberDTO newMember() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("testEmail");
        memberDTO.setMemberPassword("testPassword");
        memberDTO.setMemberName("testName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");
        return memberDTO;
    }

}
