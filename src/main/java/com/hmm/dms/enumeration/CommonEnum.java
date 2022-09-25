package com.hmm.dms.enumeration;

public class CommonEnum {

    public enum WorkflowAuthority {
        BOTH("BOTH", "Both"),
        APPROVER("APPROVER", "Approver"),
        MAKER("MAKER", "Maker");

        public String value;
        public String description;

        WorkflowAuthority(String value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
