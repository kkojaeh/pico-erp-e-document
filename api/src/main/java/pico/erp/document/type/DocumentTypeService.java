package pico.erp.document.type;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.ContentInputStream;

public interface DocumentTypeService {

  boolean exists(@Valid @NotNull DocumentTypeId id);

  DocumentTypeData get(@Valid @NotNull DocumentTypeId id);

  ContentInputStream make(@Valid @NotNull DocumentTypeRequests.MakeRequest request);

  ContentInputStream test(@Valid @NotNull DocumentTypeRequests.TestRequest request);

  void update(@Valid @NotNull DocumentTypeRequests.UpdateRequest request);


}
