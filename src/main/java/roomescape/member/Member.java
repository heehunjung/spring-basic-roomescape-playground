package roomescape.member;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final String role;

    public Member(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

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
