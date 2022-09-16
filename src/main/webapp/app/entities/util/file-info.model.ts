export interface IFileInfo {
  filename?: string;
  filesize?: number;
}

export class FileInfo implements IFileInfo {
  constructor(public filename?: string, public filesize?: number) {}
}
