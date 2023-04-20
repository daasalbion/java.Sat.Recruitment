package sat.recruitment.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {

    @JsonProperty("Normal")
    NORMAL("Normal"),
    @JsonProperty("SuperUser")
    SUPERUSER("SuperUser"),
    @JsonProperty("Premium")
    PREMIUM("Premium"),
    NONE("");

    private final String name;

    UserType(String type) {
        this.name = type;
    }

    public String getName() {
        return name;
    }

    public static UserType getValue(String name) {
        if (name == null || name.isEmpty()) return null;

        return UserType.valueOf(name.toUpperCase());
    }

}
