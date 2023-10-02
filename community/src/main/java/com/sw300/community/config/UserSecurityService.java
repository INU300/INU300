package com.sw300.community.config;


import com.sw300.community.model.Member;
import com.sw300.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> _member = memberRepository.findByEmail(email);
        if(_member.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Member member = _member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("hen715@naver.com".equals(email)){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getKey()));
        }else{
            authorities.add(new SimpleGrantedAuthority(Role.USER.getKey()));
        }
        return new User(member.getEmail(), member.getPassword(), authorities);

    }
}
