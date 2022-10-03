export interface IUserAuthority {
  userID: string;
  userName: string;
  departmentName?: string;
  roleName: string;
  workflowAuthority: number;
}
