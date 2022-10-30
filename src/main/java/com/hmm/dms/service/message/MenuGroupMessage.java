package com.hmm.dms.service.message;

import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import java.util.List;

public class MenuGroupMessage {

    private Long id;

    private String name;

    private String translateKey;

    private int orderNo;

    private List<RoleMenuAccessDTO> subMenuItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslateKey() {
        return translateKey;
    }

    public void setTranslateKey(String translateKey) {
        this.translateKey = translateKey;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public List<RoleMenuAccessDTO> getSubMenuItems() {
        return subMenuItems;
    }

    public void setSubMenuItems(List<RoleMenuAccessDTO> subMenuItems) {
        this.subMenuItems = subMenuItems;
    }
}
