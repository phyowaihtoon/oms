package creatip.oms.repository;

import creatip.oms.domain.DocumentHeader;
import creatip.oms.enumeration.CommonEnum.DocumentStatusEnum;
import creatip.oms.service.message.DocumentInquiryMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomDocumentInquiryRepositoryImpl implements CustomDocumentInquiryRepository {

    @Autowired
    private EntityManager em;

    @Override
    public Page<DocumentHeader> findDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DocumentHeader> cq = cb.createQuery(DocumentHeader.class);
        Root<DocumentHeader> root = cq.from(DocumentHeader.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        List<Order> orderList = new ArrayList<Order>();

        // Prepare for Filtering or Predicates
        Predicate predicateForMetaDataHrId = cb.equal(root.get("metaDataHeaderId"), dto.getMetaDataHeaderId());
        predicates.add(predicateForMetaDataHrId);

        String specificVal1 = dto.getFieldValue1();
        if (specificVal1 != null && !specificVal1.equals("null") && !specificVal1.isEmpty()) {
            specificVal1 = specificVal1.trim();
            Expression<String> sqlExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldIndex1())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Predicate predicate = cb.equal(sqlExpression, specificVal1);
            String searchType = dto.getFieldSearchType1();
            if (searchType != null && searchType.equals("CO")) predicate = cb.like(sqlExpression, "%" + specificVal1 + "%");
            if (searchType != null && searchType.equals("SW")) predicate = cb.like(sqlExpression, specificVal1 + "%");
            if (searchType != null && searchType.equals("EW")) predicate = cb.like(sqlExpression, "%" + specificVal1);
            predicates.add(predicate);
        }

        String specificVal2 = dto.getFieldValue2();
        if (specificVal2 != null && !specificVal2.equals("null") && !specificVal2.isEmpty()) {
            specificVal2 = specificVal2.trim();
            Expression<String> sqlExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldIndex2())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Predicate predicate = cb.equal(sqlExpression, specificVal2);
            String searchType = dto.getFieldSearchType2();
            if (searchType != null && searchType.equals("CO")) predicate = cb.like(sqlExpression, "%" + specificVal2 + "%");
            if (searchType != null && searchType.equals("SW")) predicate = cb.like(sqlExpression, specificVal2 + "%");
            if (searchType != null && searchType.equals("EW")) predicate = cb.like(sqlExpression, "%" + specificVal2);
            predicates.add(predicate);
        }

        String specificVal3 = dto.getFieldValue3();
        if (specificVal3 != null && !specificVal3.equals("null") && !specificVal3.isEmpty()) {
            specificVal3 = specificVal3.trim();
            Expression<String> sqlExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldIndex3())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Predicate predicate = cb.equal(sqlExpression, specificVal3);
            String searchType = dto.getFieldSearchType3();
            if (searchType != null && searchType.equals("CO")) predicate = cb.like(sqlExpression, "%" + specificVal3 + "%");
            if (searchType != null && searchType.equals("SW")) predicate = cb.like(sqlExpression, specificVal3 + "%");
            if (searchType != null && searchType.equals("EW")) predicate = cb.like(sqlExpression, "%" + specificVal3);
            predicates.add(predicate);
        }

        String specificVal4 = dto.getFieldValue4();
        if (specificVal4 != null && !specificVal4.equals("null") && !specificVal4.isEmpty()) {
            specificVal4 = specificVal4.trim();
            Expression<String> sqlExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldIndex4())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Predicate predicate = cb.equal(sqlExpression, specificVal4);
            String searchType = dto.getFieldSearchType4();
            if (searchType != null && searchType.equals("CO")) predicate = cb.like(sqlExpression, "%" + specificVal4 + "%");
            if (searchType != null && searchType.equals("SW")) predicate = cb.like(sqlExpression, specificVal4 + "%");
            if (searchType != null && searchType.equals("EW")) predicate = cb.like(sqlExpression, "%" + specificVal4);
            predicates.add(predicate);
        }

        String generalVal = dto.getGeneralValue();
        if (generalVal != null && !generalVal.equals("null") && !generalVal.isEmpty()) {
            generalVal = generalVal.trim();
            Predicate predicate = cb.like(root.get("fieldValues"), "%" + generalVal + "%");
            predicates.add(predicate);
        }

        Set<Integer> setOfStatus = new HashSet<Integer>();
        if (dto.getStatus() == 0) {
            for (DocumentStatusEnum enumData : DocumentStatusEnum.values()) {
                setOfStatus.add(enumData.value);
            }
        } else setOfStatus.add(dto.getStatus());

        Predicate predicateForStatus = root.get("status").in(setOfStatus);
        predicates.add(predicateForStatus);

        if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String dateFormat = "dd-MM-yyyy";
            try {
                Date criteriaDate = new SimpleDateFormat(dateFormat).parse(dto.getCreatedDate());
                Expression<Date> createdDateExpression = cb.function("DATE", Date.class, root.get("createdDate"));
                Predicate predicate = cb.equal(createdDateExpression, criteriaDate);
                predicates.add(predicate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Prepare for Ordering
        if (dto.getFieldSortBy1() != 0) {
            Expression<String> orderByExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldSortBy1())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Order orderBy = cb.asc(orderByExpression);
            orderList.add(orderBy);
        }
        if (dto.getFieldSortBy2() != 0) {
            Expression<String> orderByExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldSortBy2())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Order orderBy = cb.asc(orderByExpression);
            orderList.add(orderBy);
        }
        if (dto.getFieldSortBy3() != 0) {
            Expression<String> orderByExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldSortBy3())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Order orderBy = cb.asc(orderByExpression);
            orderList.add(orderBy);
        }
        if (dto.getFieldSortBy4() != 0) {
            Expression<String> orderByExpression = cb.function(
                "SUBSTRING_INDEX",
                String.class,
                cb.function("SUBSTRING_INDEX", String.class, root.get("fieldValues"), cb.literal("|"), cb.literal(dto.getFieldSortBy4())),
                cb.literal("|"),
                cb.literal(-1)
            );
            Order orderBy = cb.asc(orderByExpression);
            orderList.add(orderBy);
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(orderList);
        TypedQuery<DocumentHeader> query = em.createQuery(cq);

        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<DocumentHeader> resultList = query.getResultList();
        long totalCount = resultList.size(); // get the total count of records

        if (pageable != null) {
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            countQuery.select(cb.count(countQuery.from(DocumentHeader.class))).where(predicates.toArray(new Predicate[0]));
            totalCount = em.createQuery(countQuery).getSingleResult();
        }
        return new PageImpl<>(resultList, pageable, totalCount);
    }
}