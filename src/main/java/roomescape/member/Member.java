package roomescape.member;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    // todo: 빈 생성자 + 모든 필드를 필요로하는 생성자가 정확히 어디서 왜 필요한지
    public Member() {}

    public Member(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // todo : password 일부러 뺀건가 ?
    public Member(Long id, String name, String email, String role) {
        this(id, name, email, null, role);
    }

    public Member(String name, String email, String password, String role) {
        this(null, name, email, password, role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
