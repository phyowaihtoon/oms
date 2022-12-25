package com.hmm.dms.service.dto;

public class BasicLineDto {

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
        return "BasicLineDto [name=" + name + ", count=" + count + "]";
    }
}
