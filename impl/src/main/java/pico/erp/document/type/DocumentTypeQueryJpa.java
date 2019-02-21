package pico.erp.document.type;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class DocumentTypeQueryJpa implements DocumentTypeQuery {

  private final QDocumentTypeEntity documentType = QDocumentTypeEntity.documentTypeEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<DocumentTypeView> retrieve(DocumentTypeView.Filter filter, Pageable pageable) {
    val query = new JPAQuery<DocumentTypeView>(entityManager);
    val select = Projections.bean(DocumentTypeView.class,
      documentType.id,
      documentType.name,
      documentType.createdBy,
      documentType.createdDate
    );

    query.select(select);
    query.from(documentType);

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getName())) {
      builder.and(documentType.name
        .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getName(), "%")));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
