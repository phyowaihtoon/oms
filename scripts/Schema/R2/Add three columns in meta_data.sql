alter table meta_data add search_type char(1)  default('N') after search_by;
alter table meta_data add sort_by char(1) default('N') after search_type;
alter table meta_data add fieldname_in_myanmar varchar(45) after field_name;