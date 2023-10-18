package com.sw300.community.member.controller;

import com.sw300.community.member.dto.MemberSaveDto;
import com.sw300.community.member.model.Member;
import com.sw300.community.member.service.MemberService;
import com.sw300.community.model.School;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    @PostMapping("/api/join")
    public Long newMem(@Valid @RequestBody MemberSaveDto memberSaveDto){
        return memberService.joinMember(memberSaveDto);
    }

    @PostMapping("/api/changeNickname")
    public Long changeNickname(@RequestBody Map<String,String> newNickname, Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        return memberService.changeNickname(member.getId(),newNickname.get("newNickname"));
    }

    @PostMapping("/api/changePassword")
    public Long changePassword(@RequestBody Map<String,String> password,Principal principal){
        Member member = this.memberService.getMember(principal.getName());
        return memberService.changePassword(member.getId(),password.get("currentPassword"),password.get("newPassword"));
    }

    @PostMapping("/api/newPassword")
    public Long newPassword(@RequestBody Map<String,String> inform){
        System.out.println(inform.get("email"));
        return memberService.newPassword(inform.get("email"),inform.get("newPassword"));
    }

    @GetMapping("/api/getDepartmentsBySchool")
    public ResponseEntity<List<String>> getDepartmentsBySchool(@RequestParam String school){
        List<String> departments = memberService.getDepartment(school);
        System.out.println(departments.size());
        return ResponseEntity.ok(departments);
    }



/*

    // 토큰 발행, 재발행 예시 코드
    // 사용자 정보를 User로 만들었고, 반환 형식, 예외 처리 등 다른 부분이 있어서 참고만 해주셔도 될 것 같아요
    // 아시겠지만 88번 줄에 "haeun" 대신 원하시는 암호화 문자열 넣어주시면 됩니다 :)

    // 토큰 발행하기, 유효 기간 1개월 설정
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = Timestamp.valueOf(expiredDateTime);

        // 토큰 발행
        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("haeun"));

        return ResponseEntity.ok().body(
                UserLoginToken.builder().token(token).build());

    }

    // 토큰 재발행 - 토큰은 헤더에 있음 가져오기 위해 request 객체
    @PatchMapping("/api/user/login")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("TOKEN"); // 추가 헤더의 키가 TOKEN이 됨

        String email = "";
        try {
            email = JWT.require(Algorithm.HMAC512("haeun"))
                    .build()
                    .verify(token)
                    .getIssuer();
        } catch (JWTDecodeException e) {
            throw new PasswordNotMatchException("토큰이 일치하지 않습니다.");
        } catch (Exception e) {
            throw new PasswordNotMatchException("토큰 발행 중 문제가 발생했습니다.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = Timestamp.valueOf(expiredDateTime);

        String newToken = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("haeun"));

        return ResponseEntity.ok().body(
                UserLoginToken.builder().token(newToken).build());

    }

*/

}
