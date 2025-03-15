package roomescape.member;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomescapeUnauthorizedException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findByName(String name) {
        try {
            return memberDao.findByName(name);
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new RoomescapeUnauthorizedException("회원 정보를 찾을 수 없습니다.");
        }
    }
}
