package roomescape.global.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.session.cookie.CookieResolver;
import roomescape.auth.session.jwt.JwtResolver;
import roomescape.global.interceptor.AuthInterceptor;
import roomescape.member.MemberService;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtResolver jwtResolver;
    private final CookieResolver cookieResolver;
    private final AuthInterceptor authInterceptor;

    public WebConfiguration(MemberService memberService, JwtResolver jwtResolver, CookieResolver cookieResolver,
                            final AuthInterceptor authInterceptor) {
        this.memberService = memberService;
        this.jwtResolver = jwtResolver;
        this.cookieResolver = cookieResolver;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> memberArgumentResolver) {
        memberArgumentResolver.add(new MemberArgumentResolver(memberService, jwtResolver, cookieResolver));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/admin/**");
    }
}
