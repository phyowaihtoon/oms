package creatip.oms.service.message;

import creatip.oms.service.dto.RoleMenuAccessDTO;
import java.util.List;

public class MenuGroupMessage {

    private Long id;

    private String groupCode;

    private String name;

    private String translateKey;

    private String faIcon;

    private int orderNo;

    private String routerLink;

    private List<RoleMenuAccessDTO> subMenuItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
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

    public String getFaIcon() {
        return faIcon;
    }

    public void setFaIcon(String faIcon) {
        this.faIcon = faIcon;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getRouterLink() {
        return routerLink;
    }

    public void setRouterLink(String routerLink) {
        this.routerLink = routerLink;
    }

    public List<RoleMenuAccessDTO> getSubMenuItems() {
        return subMenuItems;
    }

    public void setSubMenuItems(List<RoleMenuAccessDTO> subMenuItems) {
        this.subMenuItems = subMenuItems;
    }
}
