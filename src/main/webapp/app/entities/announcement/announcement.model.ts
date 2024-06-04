export interface IAnnouncement {
  id?: number;
  description?: string;
  delFlag?: string;
  activeFlag?: string;
  //isChecked?: boolean;
}

export class Announcement implements IAnnouncement {
  constructor(
    public id?: number,
    public description?: string,
    public delFlag?: string,
    public activeFlag?: string
  ) //public isChecked?: boolean
  {}
}

export function getAnnouncementIdentifier(announcement: IAnnouncement): number | undefined {
  return announcement.id;
}
