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

export interface IMenuGroup {
  id: number;
  name: string;
  translateKey: string;
  orderNo: number;
}

export interface IMenuItem {
  id: number;
  name: string;
  translateKey: string;
  menuCode: string;
  routerLink: string;
  orderNo: number;
  menuGroup: IMenuGroup;
}
