import { IUser } from 'app/entities/user/user.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IApplicationUser {
  id?: number;
  userRole?: string;
  workflowAuthority?: string;
  user?: IUser;
  department?: IDepartment | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public userRole?: string,
    public workflowAuthority?: string,
    public user?: IUser,
    public department?: IDepartment | null
  ) {}
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}
