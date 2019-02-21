package pico.erp.document;

import pico.erp.document.type.DocumentTypeId;

public interface DocumentInfo {

  DocumentId getId();

  DocumentTypeId getTypeId();

  String getName();

  String getContentType();

  long getContentLength();

}
