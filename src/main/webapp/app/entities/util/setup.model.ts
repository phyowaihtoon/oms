export interface IWorkflowAuthority {
  value: number;
  description: string;
}
export interface IDocumentStatus {
  value: number;
  description: string;
}
export interface IPriority {
  value: number;
  description: string;
}

export interface ICodeType {
  value: string;
  description: string;
}

export interface IMenuGroup {
  id: number;
  name: string;
  translateKey: string;
  faIcon: string;
  orderNo: number;
}

export interface IMenuItem {
  id: number;
  name: string;
  translateKey: string;
  faIcon: string;
  menuCode: string;
  routerLink: string;
  orderNo: number;
  menuGroup: IMenuGroup;
}
