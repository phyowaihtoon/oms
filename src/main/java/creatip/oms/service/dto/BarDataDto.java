package creatip.oms.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BarDataDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private Long count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BarDataDto [name=" + name + ", count=" + count + "]";
    }

    public static BarDataDto toDTO(Object[] object) {
        BarDataDto dto = new BarDataDto();
        dto.setName(((String) object[1]));
        dto.setCount(((BigInteger) object[0]).longValue());
        return dto;
    }
}
