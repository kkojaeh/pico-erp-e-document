package pico.erp.document.subject;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentSubjectQuery {

  Page<DocumentSubjectView> retrieve(@NotNull DocumentSubjectView.Filter filter,
    @NotNull Pageable pageable);

}
