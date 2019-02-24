package pico.erp.document.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import pico.erp.document.DocumentInfo;

public class FileSystemDocumentStorageStrategy implements DocumentStorageStrategy {

  @Setter
  @Value("${document.storage.root-dir}")
  private File rootDir;

  @Override
  public boolean exists(DocumentStorageKey storageKey) {
    return new File(rootDir, storageKey.getValue()).exists();
  }

  @Override
  @SneakyThrows
  public InputStream load(DocumentStorageKey storageKey) {
    File file = new File(rootDir, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      return new FileInputStream(file);
    }
  }

  @Override
  public void remove(DocumentStorageKey storageKey) {
    File file = new File(rootDir, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      file.delete();
    }
  }

  @Override
  @SneakyThrows
  public DocumentStorageKey save(DocumentInfo info, InputStream inputStream) {
    File destFile = new File(rootDir, info.getId().getValue().toString());
    FileUtils.copyInputStreamToFile(inputStream, destFile);
    return DocumentStorageKey.from(rootDir.toURI().relativize(destFile.toURI()).toString());
  }
}
