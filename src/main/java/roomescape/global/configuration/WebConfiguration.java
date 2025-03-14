package roomescape.global.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.session.cookie.CookieResolver;
import roomescape.auth.session.jwt.JwtResolver;
import roomescape.member.MemberService;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtResolver jwtResolver;
    private final CookieResolver cookieResolver;

    public WebConfiguration(MemberService memberService, JwtResolver jwtResolver, CookieResolver cookieResolver) {
        this.memberService = memberService;
        this.jwtResolver = jwtResolver;
        this.cookieResolver = cookieResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> memberArgumentResolver) {
        memberArgumentResolver.add(new MemberArgumentResolver(memberService, jwtResolver, cookieResolver));
    }
}
