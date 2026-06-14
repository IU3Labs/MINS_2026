package service.dto;
public record UserInfo(String name, String email) {
    public static UserInfo unknown() {
        return new UserInfo("Unknown", "N/A");
    }
}