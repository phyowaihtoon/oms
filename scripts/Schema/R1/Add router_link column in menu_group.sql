alter table menu_group add column router_link varchar(100);
update menu_group set router_link='report/doclist-rpt' where group_code='RPTMG' and id>0;