import { IMenuGroupMessage, IRoleMenuAccess } from 'app/entities/user-role/user-role.model';
import { IDashboardTemplate } from 'app/services/dashboard-template.model';

export interface IUserAuthority {
  userID?: string;
  userName?: string;
  roleID?: number;
  roleName?: string;
  roleType?: number;
  workflowAuthority?: number;
  sysConfigMessage?: ISysConfigMessage;
  menuGroups?: IMenuGroupMessage[];
  activeMenu?: IRoleMenuAccess;
  dashboardTemplates?: IDashboardTemplate[];
  departmentId?: number;
  departmentName?: string;
}

export class UserAuthority implements IUserAuthority {
  constructor(
    public userID?: string,
    public userName?: string,
    public roleID?: number,
    public roleName?: string,
    public roleType?: number,
    public workflowAuthority?: number,
    public sysConfigMessage?: ISysConfigMessage,
    public menuGroups?: IMenuGroupMessage[],
    public activeMenu?: IRoleMenuAccess,
    public dashboardTemplates?: IDashboardTemplate[],
    public departmentId?: number,
    public departmentName?: string
  ) {}
}

export interface ISysConfigMessage {
  workflowEnabled: string;
}
