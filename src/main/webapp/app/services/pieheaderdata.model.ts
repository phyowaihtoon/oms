export interface IPieHeaderDataDto {
  totalCount?: number;
  data?: IPieData[];
}

export interface IPieData {
  name?: string;
  data?: number;
}

export class PieHeaderDataDto implements IPieHeaderDataDto {
  constructor(public totalCount?: number, public pieData?: IPieData[]) {}
}

export class PieData implements IPieData {
  constructor(public name?: string, public data?: number) {}
}
