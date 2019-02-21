package pico.erp.document.storage;

import java.io.InputStream;
import pico.erp.document.DocumentInfo;

public interface DocumentStorageStrategy {

  boolean exists(DocumentStorageKey storageKey);

  InputStream load(DocumentStorageKey storageKey);

  void remove(DocumentStorageKey storageKey);

  DocumentStorageKey save(DocumentInfo info, InputStream inputStream);

}
