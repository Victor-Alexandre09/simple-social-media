package simple.social.media.api.domain.User;

public record UserDTO(
        Long id,
        String name,
        String password) {

    public UserDTO (User user) {
        this(user.getId(), user.getName(), user.getPassword());
    }
}
