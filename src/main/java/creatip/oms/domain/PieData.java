package creatip.oms.domain;

import java.io.Serializable;
import java.math.BigInteger;

public class PieData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long count;
    private Integer status;

    public Long getCount() {
        return count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PieData [count=" + count + ", status=" + status + "]";
    }

    public static PieData toDTO(Object[] object) {
        PieData dto = new PieData();
        dto.setCount(((BigInteger) object[0]).longValue());
        dto.setStatus(((Integer) object[1]).intValue());
        return dto;
    }
}
