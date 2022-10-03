package com.hmm.dms.enumeration;

public class CommonEnum {

    public enum WorkflowAuthorityEnum {
        MAKER(1, "Maker"),
        APPROVER(2, "Approver"),
        BOTH(3, "Both");

        public int value;
        public String description;

        WorkflowAuthorityEnum(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    public enum DocumentStatusEnum {
        NEW(1, "New"),
        CANCEL(2, "Canceled"),
        AMEND(3, "Pending for Amendment"),
        APPROVE(4, "Approved"),
        REJECT(5, "Rejected");

        public int value;
        public String description;

        DocumentStatusEnum(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    public enum PriorityEnum {
        NA(0, "NA"),
        LOW(1, "Low"),
        MEDIUM(2, "Medium"),
        HIGH(3, "High");

        public int value;
        public String description;

        PriorityEnum(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
