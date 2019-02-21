package pico.erp.document;

import pico.erp.document.type.DocumentTypeId;

public interface DocumentInfo {

  long getContentLength();

  String getContentType();

  DocumentId getId();

  String getName();

  DocumentTypeId getTypeId();

}
