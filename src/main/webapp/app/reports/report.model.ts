export interface IRptParamsDTO {
  rptTitleName?: string;
  rptFileName?: string;
  rptFormat?: string;
  rptPath?: string;
  rptJrxml?: string;
  rptPS1?: string;
  rptPS2?: string;
  rptPS3?: string;
  rptPS4?: string;
  rptPS5?: string;
}

export class RptParamsDTO implements IRptParamsDTO {
  constructor(
    public rptTitleName?: string,
    public rptFileName?: string,
    public rptFormat?: string,
    public rptPath?: string,
    public rptJrxml?: string,
    public rptPS1?: string,
    public rptPS2?: string,
    public rptPS3?: string,
    public rptPS4?: string,
    public rptPS5?: string
  ) {}
}
