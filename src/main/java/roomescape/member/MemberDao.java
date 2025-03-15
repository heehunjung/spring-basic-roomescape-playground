package roomescape.member;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("role")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member save(Member member) {
        Map<String, Object> parameters = getMemberMap(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Member(id, member.getName(), member.getEmail(), member.getRole());
    }

    public Member findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM member WHERE email = ? AND password = ?";

        return jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email, password);
    }

    public Member findByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";

        return jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, name);
    }

    private Map<String, Object> getMemberMap(Member member) {
        Map<String, Object> parameters = Map.of(
                "name", member.getName(),
                "email", member.getEmail(),
                "password", member.getPassword(),
                "role", member.getRole()
        );
        return parameters;
    }
}
