package creatip.oms.enumeration;

public class CommonEnum {

    public enum DeliveryStatus {
        SAVE_AS_DRAFT((short) 0, "Save As Draft"),
        SENT((short) 1, "Sent");

        public short value;
        public String description;

        DeliveryStatus(short value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    public enum ViewStatus {
        UNREAD(0, "UnRead"),
        READ(1, "Read");

        public int value;
        public String description;

        ViewStatus(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    public enum ReceiverType {
        MAIN(1, "Main"),
        CC(2, "Cc");

        public int value;
        public String description;

        ReceiverType(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }

    public enum MeetingStatus {
        CREATED(1, "Created"),
        CANCEL(2, "Cancelled"),
        START(3, "Started"),
        FINISH(4, "Finished");

        public int value;
        public String description;

        MeetingStatus(int value, String description) {
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
