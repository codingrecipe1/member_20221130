package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "memberPages/memberMain";
        } else {
            return "memberPages/memberLogin";
        }
    }

    @GetMapping("/main")
    public String mainPage() {
        return "memberPages/memberMain";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/memberList";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/update")
    public String updateForm(Model model, HttpSession session) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "memberPages/memberMain";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/member/";
    }

    @PostMapping("/dup-check")
//    public @ResponseBody String emailDuplicateCheck(@RequestParam("inputEmail") String memberEmail) {
    public ResponseEntity emailDuplicateCheck(@RequestParam("inputEmail") String memberEmail) {
        String checkResult = memberService.emailDuplicateCheck(memberEmail);
//        return checkResult;
        if (checkResult != null) {
            return new ResponseEntity<>("사용해도 됩니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("사용할 수 없습니다.", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/ajax/{id}")
    public ResponseEntity findByIdAxios(@PathVariable Long id) {
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}












