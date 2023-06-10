package creatip.oms.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class PieData2 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long count;
    private String name;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PieData2 [count=" + count + ", name=" + name + "]";
    }

    public static PieData2 toDTO(Object[] object) {
        PieData2 dto = new PieData2();
        dto.setCount(((BigDecimal) object[0]).longValue());
        //dto.setCount(((BigInteger) object[0]).longValue());
        dto.setName(((String) object[1]));
        return dto;
    }
}
