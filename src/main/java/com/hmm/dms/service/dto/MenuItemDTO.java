package com.hmm.dms.service.dto;

public class MenuItemDTO {

    private Long id;

    private String name;

    private String translateKey;

    private String menuCode;

    private String routerLink;

    private int orderNo;

    private String faIcon;

    private MenuGroupDTO menuGroup;

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

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getRouterLink() {
        return routerLink;
    }

    public void setRouterLink(String routerLink) {
        this.routerLink = routerLink;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getFaIcon() {
        return faIcon;
    }

    public void setFaIcon(String faIcon) {
        this.faIcon = faIcon;
    }

    public MenuGroupDTO getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroupDTO menuGroup) {
        this.menuGroup = menuGroup;
    }

    @Override
    public String toString() {
        return (
            "MenuItemDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ",translateKey='" +
            getTranslateKey() +
            "'" +
            ",menuCode='" +
            getMenuCode() +
            "'" +
            ",routerLink='" +
            getRouterLink() +
            "'" +
            ",orderNo='" +
            getOrderNo() +
            "'" +
            ",faIcon='" +
            getFaIcon() +
            "'" +
            "}"
        );
    }
}
