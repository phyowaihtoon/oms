CREATE DEFINER=`root`@`localhost` PROCEDURE `DocumentDeliveryListRpt`(IN frmDate varchar(10),IN toDate varchar(10), 
IN deptid bigint, IN loginDeptid bigint, IN docstatus int)
BEGIN
	IF  deptid = 0 and docstatus = 2 THEN
		SELECT DATE(dd.created_date), dd.reference_no, dd.Subject, d.department_name,
        CASE
			WHEN dr.status = 1 THEN 'ဖတ်ပြီး'
			WHEN dr.status = 0 THEN 'မဖတ်ရသေး'
        ELSE 
			'unknown' -- Handle any other values as needed
		END AS status_description    
		FROM document_delivery dd        
		LEFT JOIN document_receiver dr ON dr.header_id = dd.id
		LEFT JOIN department d ON d.id = dr.receiver_id
		WHERE DATE(dd.created_date) >= STR_TO_DATE(frmDate, '%d-%m-%Y')  
		AND DATE(dd.created_date) <= STR_TO_DATE(toDate, '%d-%m-%Y')
        AND dr.receiver_id = loginDeptid;
	
    ELSEIF  deptid <> 0 and docstatus <> 2 THEN
		SELECT DATE(dd.created_date), dd.reference_no, dd.Subject, d.department_name,
        CASE
			WHEN dr.status = 1 THEN 'ဖတ်ပြီး'
			WHEN dr.status = 0 THEN 'မဖတ်ရသေး'
        ELSE 
			'unknown' -- Handle any other values as needed
		END AS status_description    
		FROM document_delivery dd        
		LEFT JOIN document_receiver dr ON dr.header_id = dd.id
		LEFT JOIN department d ON d.id = dr.receiver_id
		WHERE DATE(dd.created_date) >= STR_TO_DATE(frmDate, '%d-%m-%Y')  
		AND DATE(dd.created_date) <= STR_TO_DATE(toDate, '%d-%m-%Y')
        AND dr.receiver_id = loginDeptid AND dr.status = docstatus
        AND dd.sender_id = deptid;
	
     ELSEIF  deptid <> 0 and docstatus = 2 THEN
		SELECT DATE(dd.created_date), dd.reference_no, dd.Subject, d.department_name,
        CASE
			WHEN dr.status = 1 THEN 'ဖတ်ပြီး'
			WHEN dr.status = 0 THEN 'မဖတ်ရသေး'
        ELSE 
			'unknown' -- Handle any other values as needed
		END AS status_description    
		FROM document_delivery dd        
		LEFT JOIN document_receiver dr ON dr.header_id = dd.id
		LEFT JOIN department d ON d.id = dr.receiver_id
		WHERE DATE(dd.created_date) >= STR_TO_DATE(frmDate, '%d-%m-%Y')  
		AND DATE(dd.created_date) <= STR_TO_DATE(toDate, '%d-%m-%Y')
        AND dr.receiver_id = loginDeptid AND dd.sender_id = deptid;
        
	ELSE  
		SELECT DATE(dd.created_date), dd.reference_no, dd.Subject, d.department_name, 
        CASE
			WHEN dr.status = 1 THEN 'ဖတ်ပြီး'
			WHEN dr.status = 0 THEN 'မဖတ်ရသေး'
        ELSE 
			'unknown' -- Handle any other values as needed
		END AS status_description 
		FROM document_delivery dd        
		LEFT JOIN document_receiver dr ON dr.header_id = dd.id
		LEFT JOIN department d ON d.id = dr.receiver_id
		WHERE DATE(dd.created_date) >= STR_TO_DATE(frmDate, '%d-%m-%Y')  
		AND DATE(dd.created_date) <= STR_TO_DATE(toDate, '%d-%m-%Y')
        AND dr.receiver_id = loginDeptid AND dr.status = docstatus;      
	
    
    END IF; 
END