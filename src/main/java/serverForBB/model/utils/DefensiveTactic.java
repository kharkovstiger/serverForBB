package serverForBB.model.utils;

public enum DefensiveTactic {
    MAN_TO_MAN("Man to Man"),
    TWO_THREE_ZONE("2-3 Zone"),
    THREE_TWO_ZONE("3-2 Zone"),
    ONE_THREE_ONE_ZONE("1-3-1 Zone"),
    FULL_COURT_PRESS("Full Court Press"),
    INSIDE_BOX("Inside Box and One"),
    OUTSIDE_BOX("Outside Box and One");
    
    private String bbname;

    DefensiveTactic(String name) {
        this.bbname =name;
    }

    public String bbname() {
        return bbname;
    }

    public static DefensiveTactic fromString(String text) {
        for (DefensiveTactic b : DefensiveTactic.values()) {
            if (b.bbname.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
