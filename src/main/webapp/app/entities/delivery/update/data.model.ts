
export interface IData{
  id?: number;
  subject?: string;
  desc?: string;
  file?: IFileInfo[];
  mainDept?: IDepartment[];
  ccDept?: IDepartment[];

}

export class Data implements IData {
  constructor(
    public id?: number,
    public subject?: string,
    public desc?: string,
    public file?: IFileInfo[],
    public mainDept?: IDepartment[],
    public ccDept?: IDepartment[]
  ) {}
}

export interface IFileInfo {
  filePath?: string;
  fileName?: string;
  fileSize?: number;
  fileData?: File; 
}

export class FileInfo implements IFileInfo{
  constructor(
    public filePath?: string,
    public fileName?: string,
    public fileSize?: number,
    public fileData?: File, 
  ){}
}

export interface IDepartment {
  deptID?: string;
  deptName?: string;
}

export class Department implements IDepartment{
  constructor(
    public deptID?: string,
    public deptName?: string,
  ){}
}
