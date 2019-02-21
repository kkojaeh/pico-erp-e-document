package pico.erp.document.type;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentTypeQuery {

  Page<DocumentTypeView> retrieve(@NotNull DocumentTypeView.Filter filter,
    @NotNull Pageable pageable);

}
