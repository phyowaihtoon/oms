package creatip.oms.enumeration;

public class CommonEnum {

    public enum WorkflowAuthorityEnum {
        NA(0, "NA"),
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
        NA(0, "NA"),
        NEW(1, "New"),
        SEND(2, "Sent for Approval"),
        CANCEL(3, "Canceled"),
        AMEND(4, "Sent for Amendment"),
        APPROVE(5, "Approved"),
        REJECT(6, "Rejected");

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

    public enum CodeTypeEnum {
        CIVIL("CT001", "တရားမ Civil"),
        CRIMINAL("CT002", "ပြစ်မှု Criminal"),
        WRIT("CT003", "စာချွန်တော်"),
        LAWYER("CT004", "ရှေ့နေရှေ့ရပ်");

        public String value;
        public String description;

        CodeTypeEnum(String value, String description) {
            this.value = value;
            this.description = description;
        }

        public static CodeTypeEnum findByName(String value) {
            CodeTypeEnum result = null;
            for (CodeTypeEnum codeType : values()) {
                if (codeType.value.equalsIgnoreCase(value)) {
                    result = codeType;
                    break;
                }
            }
            return result;
        }
    }
}
