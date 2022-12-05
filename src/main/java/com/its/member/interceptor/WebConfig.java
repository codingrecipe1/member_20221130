package com.its.member.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 해당 클래스의 설정 정보를 스프링 컨테이너에 등록
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor()) // 인터셉터로 등록할 클래스
                .order(1) // 해당 인터셉터의 우선순위
                .addPathPatterns("/**") // 인터셉터로 체크할 주소(모든 주소)
                .excludePathPatterns("/", "/member/save", "/member/login",
                                    "/js/**", "/css/**", "/error", "/images/**",
                                    "/*.ico", "/favicon/**"); // 예외로 할 주소

    }
}
