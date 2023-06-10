package creatip.oms.service.dto;

public class PieDataDto {

    private String name;
    private double data;

    public PieDataDto(String name, double data) {
        super();
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PieDataDto [name=" + name + ", data=" + data + "]";
    }
}
