package pico.erp.document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.ContentInputStream;

public interface DocumentService {

  DocumentData create(@Valid DocumentRequests.CreateRequest request);

  void delete(@Valid DocumentRequests.DeleteRequest request);

  boolean exists(@NotNull DocumentId id);

  DocumentData get(@NotNull DocumentId id);

  ContentInputStream load(@NotNull DocumentId id);

}
