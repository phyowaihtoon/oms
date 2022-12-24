package com.hmm.dms.repository;

import com.hmm.dms.domain.PieData;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public class DashboardRepository {

    @Autowired
    EntityManager entityManager;

    public List<PieData> getDocumentSummary() {
        Query query = entityManager.createNativeQuery(
            "select  count(id) as count, status " + " from document_header " + " where del_flag = 'N' group by status order by status"
        );
        List<Object[]> objList = query.getResultList();
        List<PieData> retList = objList.stream().map(PieData::toDTO).collect(Collectors.toList());
        return retList;
    }

    public List<PieData> getTodaySummary() {
        Query query = entityManager.createNativeQuery(
            "select  count(id) as count, status " +
            " from document_header " +
            " where del_flag = 'N' and created_date > DATE_FORMAT(SYSDATE(), \"%Y-%m-%d\") " +
            " group by status order by status"
        );
        List<Object[]> objList = query.getResultList();
        List<PieData> retList = objList.stream().map(PieData::toDTO).collect(Collectors.toList());
        return retList;
    }
}
