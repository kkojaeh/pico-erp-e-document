package pico.erp.document.subject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface DocumentSubjectExceptions {

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "e-document-type.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
