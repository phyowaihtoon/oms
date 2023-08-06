package creatip.oms.enumeration;

public class CommonEnum {

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
