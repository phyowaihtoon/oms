alter table dashboard_template add column name_in_myanmar varchar(255);
update dashboard_template set name_in_myanmar=card_name where id>0;
update dashboard_template set name_in_myanmar=N'စာရင်းအချက်အလက် အခြေပြဇယား' where card_id='CARD006';
insert into dashboard_template values ('8', 'CARD008', 'Document Inquiry', 'data', '','Document Inquiry');