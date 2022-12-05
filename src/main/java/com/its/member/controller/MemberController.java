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

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "redirectURL", defaultValue = "/member/main") String redirectURL,
                            Model model) {
        model.addAttribute("redirectURL", redirectURL);
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        @RequestParam(value = "redirectURL", defaultValue = "/member/main") String redirectURL) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            // 인터셉터에 걸려서 로그인한 사용자가 직전에 요청한 페이지로 보내주기 위해서 redirect:/직전요청주소
            // 인터셉터 걸리지 않고 로그인을 하는 사용자는 defaultValue에 의해서 main으로
            return "redirect:" + redirectURL;
//            return "memberPages/memberMain";
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

    /*
        get: /member/{id}
        post: /member/{id}
        delete: /member/{id}
        put: /member/{id}
     */

    @DeleteMapping("/{id}")
    public ResponseEntity deleteByAxios(@PathVariable Long id) {
        System.out.println("id = " + id);
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateByAxios(@PathVariable Long id,
                                        @RequestBody MemberDTO memberDTO) {
        System.out.println("id = " + id + ", memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}












