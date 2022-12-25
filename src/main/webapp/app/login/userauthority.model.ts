import { IMenuGroupMessage, IRoleMenuAccess } from 'app/entities/user-role/user-role.model';
import { IDashboardTemplate } from 'app/services/dashboard-template.model';

export interface IUserAuthority {
  userID: string;
  userName: string;
  departmentName?: string;
  roleID: number;
  roleName: string;
  workflowAuthority: number;
  sysConfigMessage: ISysConfigMessage;
  menuGroups: IMenuGroupMessage[];
  activeMenu: IRoleMenuAccess;
  dashboardTemplates: IDashboardTemplate[];
}

export interface ISysConfigMessage {
  workflowEnabled: string;
}
