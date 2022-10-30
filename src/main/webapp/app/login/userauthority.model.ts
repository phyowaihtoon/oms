import { IRoleMenuAccess } from 'app/entities/user-role/user-role.model';

export interface IUserAuthority {
  userID: string;
  userName: string;
  departmentName?: string;
  roleName: string;
  workflowAuthority: number;
  menuGroups: IRoleMenuAccess[];
}
