CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_DOCLIST_RPT`(IN frmDate varchar(10),IN toDate varchar(10), 
IN metadata bigint, IN userid varchar(50),IN loginUserId bigint)
BEGIN
	IF userid = '' and metadata = 0 THEN
		
			SELECT doc.file_name, doc.file_path, meta_head.doc_title, doc.created_by, date(doc.created_date), date(doc.last_modified_date), doc_head.message
			FROM document doc 
			inner join document_header doc_head on doc_head.id = doc.header_id
			inner join meta_data_header meta_head on meta_head.id = doc_head.meta_data_header_id
			where  date(doc.created_date) >= str_to_date(frmDate,'%d-%m-%Y')  
			and date(doc.created_date) <= str_to_date(toDate,'%d-%m-%Y')
            and doc_head.meta_data_header_id in (select rta.meta_data_header_id from role_template_access rta, application_user au
			where rta.user_role_id=au.user_role_id and au.user_id=loginUserId);
	ELSE IF userid = '' and metadata >0 THEN
		
			SELECT doc.file_name, doc.file_path, meta_head.doc_title, doc.created_by, date(doc.created_date), date(doc.last_modified_date), doc_head.message
			FROM document doc 
			left join document_header doc_head on doc_head.id = doc.header_id
			left join meta_data_header meta_head on meta_head.id = doc_head.meta_data_header_id
			where  date(doc.created_date) >= str_to_date(frmDate,'%d-%m-%Y')  
			and date(doc.created_date) <= str_to_date(toDate,'%d-%m-%Y')
            and meta_head.id = metadata;
        
	ELSE IF metadata = 0 and userid <> '' THEN
		
			SELECT doc.file_name, doc.file_path, meta_head.doc_title, doc.created_by, date(doc.created_date), date(doc.last_modified_date), doc_head.message
			FROM document doc 
			inner join document_header doc_head on doc_head.id = doc.header_id
			inner join meta_data_header meta_head on meta_head.id = doc_head.meta_data_header_id
			where  date(doc.created_date) >= str_to_date(frmDate,'%d-%m-%Y')  
			and date(doc.created_date) <= str_to_date(toDate,'%d-%m-%Y')            
            and doc.created_by = userid
            and doc_head.meta_data_header_id in (select rta.meta_data_header_id from role_template_access rta, application_user au
			where rta.user_role_id=au.user_role_id and au.user_id=loginUserId);
	
    ELSE
			SELECT doc.file_name, doc.file_path, meta_head.doc_title, doc.created_by, date(doc.created_date), date(doc.last_modified_date), doc_head.message
			FROM document doc 
			left join document_header doc_head on doc_head.id = doc.header_id
			left join meta_data_header meta_head on meta_head.id = doc_head.meta_data_header_id
			where  date(doc.created_date) >= str_to_date(frmDate,'%d-%m-%Y')  
			and date(doc.created_date) <= str_to_date(toDate,'%d-%m-%Y')            
            and doc.created_by = userid and meta_head.id = metadata;            
			END IF;  	
		END IF;
	END IF;    
END