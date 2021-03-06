package pico.erp.document.subject;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@ComponentBean
@Transactional(readOnly = true)
@Validated
public class DocumentSubjectQueryJpa implements DocumentSubjectQuery {

  private final QDocumentSubjectEntity documentSubject = QDocumentSubjectEntity.documentSubjectEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<DocumentSubjectView> retrieve(DocumentSubjectView.Filter filter, Pageable pageable) {
    val query = new JPAQuery<DocumentSubjectView>(entityManager);
    val select = Projections.bean(DocumentSubjectView.class,
      documentSubject.id,
      documentSubject.name,
      documentSubject.createdBy,
      documentSubject.createdDate
    );

    query.select(select);
    query.from(documentSubject);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getName())) {
      builder.and(documentSubject.name
        .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getName(), "%")));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
