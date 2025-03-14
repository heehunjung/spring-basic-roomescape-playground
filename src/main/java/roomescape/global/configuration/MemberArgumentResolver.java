package roomescape.global.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.AuthMember;
import roomescape.auth.session.cookie.CookieResolver;
import roomescape.auth.session.jwt.JwtResolver;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.member.MemberService;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtResolver jwtResolver;
    private final CookieResolver cookieResolver;

    public MemberArgumentResolver(MemberService memberService, JwtResolver jwtResolver,
                                  CookieResolver cookieResolver) {
        this.memberService = memberService;
        this.jwtResolver = jwtResolver;
        this.cookieResolver = cookieResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = cookieResolver.getToken(request.getCookies());
        if (accessToken == null) {
            throw new RoomescapeBadRequestException("토큰이 잘못되었습니다.");
        }
        String name = jwtResolver.resolveToken(accessToken);

        return memberService.findByName(name);
    }
}
