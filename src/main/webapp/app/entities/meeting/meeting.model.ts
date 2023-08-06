export interface IMeeting {
  id?: number;
  delFlag?: string;
}

export class Meeting implements IMeeting {
  constructor(public id?: number, public delFlag?: string) {}
}

export function getMeetingIdentifier(department: IMeeting): number | undefined {
  return department.id;
}
