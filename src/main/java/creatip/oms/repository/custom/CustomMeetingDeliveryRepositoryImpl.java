package creatip.oms.repository.custom;

import creatip.oms.domain.Department;
import creatip.oms.domain.DocumentDelivery;
import creatip.oms.domain.MeetingDelivery;
import creatip.oms.domain.MeetingReceiver;
import creatip.oms.enumeration.CommonEnum.ViewStatus;
import creatip.oms.service.message.SearchCriteriaMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMeetingDeliveryRepositoryImpl implements CustomMeetingDeliveryRepository {

    @Autowired
    private EntityManager em;

    @Override
    public Page<MeetingDelivery> findReceivedMeetingList(SearchCriteriaMessage criteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

        Root<MeetingReceiver> receiver = criteriaQuery.from(MeetingReceiver.class);

        Join<MeetingReceiver, MeetingDelivery> delivery = receiver.join("header");

        Join<MeetingReceiver, Department> receiverDepartment = receiver.join("receiver");

        List<Predicate> predicates = preparePredicates(criteria, criteriaBuilder, receiver, delivery, receiverDepartment);

        criteriaQuery.multiselect(delivery);
        criteriaQuery.distinct(true);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Tuple> query = em.createQuery(criteriaQuery);

        if (pageable != null) {
            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());
        }

        List<Tuple> resultList = query.getResultList();
        List<MeetingDelivery> meetingReceivedList = null;
        if (resultList != null) {
            meetingReceivedList = new ArrayList<MeetingDelivery>();
            for (Tuple tuple : resultList) {
                MeetingDelivery data = (MeetingDelivery) tuple.get(0);
                meetingReceivedList.add(data);
            }
        }

        long totalCount = 0;

        if (pageable != null) {
            CriteriaBuilder builder = em.getCriteriaBuilder();

            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

            Root<MeetingReceiver> receiver_count = countQuery.from(MeetingReceiver.class);

            Join<MeetingReceiver, MeetingDelivery> delivery_count = receiver_count.join("header");

            Join<MeetingReceiver, Department> receiverDep_count = receiver_count.join("receiver");

            List<Predicate> predicates_count = preparePredicates(criteria, builder, receiver_count, delivery_count, receiverDep_count);

            countQuery.select(builder.countDistinct(delivery_count)).where(predicates_count.toArray(new Predicate[0]));
            TypedQuery<Long> typedQuery = em.createQuery(countQuery);
            totalCount = typedQuery.getSingleResult();
        }
        return new PageImpl<>(meetingReceivedList, pageable, totalCount);
    }

    private List<Predicate> preparePredicates(
        SearchCriteriaMessage criteria,
        CriteriaBuilder criteriaBuilder,
        Root<MeetingReceiver> receiver,
        Join<MeetingReceiver, MeetingDelivery> delivery,
        Join<MeetingReceiver, Department> receiverDepartment
    ) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        // Prepare for Filtering or Predicates
        Predicate delFlagReceiver = criteriaBuilder.equal(receiver.get("delFlag"), "N");
        predicates.add(delFlagReceiver);

        Predicate receiverId = criteriaBuilder.equal(receiverDepartment.get("id"), criteria.getReceiverId());
        predicates.add(receiverId);

        if (criteria.getStatus() == ViewStatus.READ.value || criteria.getStatus() == ViewStatus.UNREAD.value) {
            Predicate receiverStatus = criteriaBuilder.equal(receiver.get("status"), criteria.getStatus());
            predicates.add(receiverStatus);
        }

        Predicate deliveryStatus = criteriaBuilder.equal(delivery.get("deliveryStatus"), 1);
        predicates.add(deliveryStatus);

        Predicate delFlagDelivery = criteriaBuilder.equal(delivery.get("delFlag"), "N");
        predicates.add(delFlagDelivery);

        if (criteria.getSenderId() > 0) {
            Join<DocumentDelivery, Department> senderDepartment = delivery.join("sender");
            Predicate senderId = criteriaBuilder.equal(senderDepartment.get("id"), criteria.getSenderId());
            predicates.add(senderId);
        }

        if (criteria.getDateFrom() != null && criteria.getDateFrom().trim().length() > 0) {
            String dateFormat = "dd-MM-yyyy";
            if (criteria.getDateTo() != null && criteria.getDateTo().trim().length() > 0) {
                try {
                    Expression<Date> sentDateExpression = criteriaBuilder.function("DATE", Date.class, delivery.get("sentDate"));

                    Date startDate = new SimpleDateFormat(dateFormat).parse(criteria.getDateFrom());
                    Date endDate = new SimpleDateFormat(dateFormat).parse(criteria.getDateTo());

                    Predicate predicate = criteriaBuilder.between(sentDateExpression, startDate, endDate);
                    predicates.add(predicate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        String subject = criteria.getSubject();
        if (subject != null && !subject.equals("null") && !subject.isEmpty()) {
            subject = subject.trim();
            Predicate predicate = criteriaBuilder.like(delivery.get("subject"), "%" + subject + "%");
            predicates.add(predicate);
        }

        String referenceNo = criteria.getReferenceNo();
        if (referenceNo != null && !referenceNo.equals("null") && !referenceNo.isEmpty()) {
            referenceNo = referenceNo.trim();
            Predicate predicate = criteriaBuilder.like(delivery.get("referenceNo"), "%" + referenceNo + "%");
            predicates.add(predicate);
        }

        return predicates;
    }
}
