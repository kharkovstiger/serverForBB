package serverForBB.model.utils;

public enum OffensiveTactic {
    BASE_OFFENSE("Base Offense"),
    PUSH_THE_BALL("Push the Ball"),
    PATIENT("Patient"),
    LOOK_INSIDE("Look Inside"),
    LOW_POST("Low Post"),
    MOTION("Motion"),
    RUN_N_GUN("Run and Gun"),
    PRINCETON("Princeton"),
    INSIDE_ISOLATION("Inside Isolation"),
    OUTSIDE_ISOLATION("Outside Isolation");

    private String bbname;

    OffensiveTactic(String name) {
        this.bbname =name;
    }

    public String bbname() {
        return bbname;
    }

    public static OffensiveTactic fromString(String text) {
        for (OffensiveTactic b : OffensiveTactic.values()) {
            if (b.bbname.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
