import { IDepartment } from 'app/entities/department/department.model';
import { IMenuGroupMessage, IRoleMenuAccess } from 'app/entities/user-role/user-role.model';
import { IDashboardTemplate } from 'app/services/dashboard-template.model';

export interface IUserAuthority {
  userID?: string;
  userName?: string;
  roleID?: number;
  roleName?: string;
  roleType?: number;
  sysConfigMessage?: ISysConfigMessage;
  menuGroups?: IMenuGroupMessage[];
  activeMenu?: IRoleMenuAccess;
  dashboardTemplates?: IDashboardTemplate[];
  department?: IDepartment;
}

export class UserAuthority implements IUserAuthority {
  constructor(
    public userID?: string,
    public userName?: string,
    public roleID?: number,
    public roleName?: string,
    public roleType?: number,
    public sysConfigMessage?: ISysConfigMessage,
    public menuGroups?: IMenuGroupMessage[],
    public activeMenu?: IRoleMenuAccess,
    public dashboardTemplates?: IDashboardTemplate[],
    public department?: IDepartment
  ) {}
}

export interface ISysConfigMessage {
  workflowEnabled: string;
}

export interface IUserNotification {
  id?: number;
  referenceNo?: string;
  subject?: string;
  senderName?: string;
}
