package pico.erp.document.subject;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.ContentInputStream;

public interface DocumentSubjectService {

  boolean exists(@Valid @NotNull DocumentSubjectId id);

  DocumentSubjectData get(@Valid @NotNull DocumentSubjectId id);

  ContentInputStream make(@Valid @NotNull DocumentSubjectRequests.MakeRequest request);

  ContentInputStream test(@Valid @NotNull DocumentSubjectRequests.TestRequest request);

  void update(@Valid @NotNull DocumentSubjectRequests.UpdateRequest request);


}
