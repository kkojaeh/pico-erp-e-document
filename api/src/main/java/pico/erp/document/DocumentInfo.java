package pico.erp.document;

import pico.erp.document.subject.DocumentSubjectId;

public interface DocumentInfo {

  long getContentLength();

  String getContentType();

  DocumentId getId();

  DocumentSubjectId getSubjectId();

  String getContentName();

}
