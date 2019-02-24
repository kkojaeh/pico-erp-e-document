package pico.erp.document.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import pico.erp.document.DocumentInfo;

public class FileSystemDocumentStorageStrategy implements DocumentStorageStrategy {

  private final File rootDirectory;

  public FileSystemDocumentStorageStrategy(Config config) {
    rootDirectory = config.rootDirectory;
  }

  @Override
  public boolean exists(DocumentStorageKey storageKey) {
    return new File(rootDirectory, storageKey.getValue()).exists();
  }

  @Override
  @SneakyThrows
  public InputStream load(DocumentStorageKey storageKey) {
    File file = new File(rootDirectory, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      return new FileInputStream(file);
    }
  }

  @Override
  public void remove(DocumentStorageKey storageKey) {
    File file = new File(rootDirectory, storageKey.getValue());
    if (!file.exists()) {
      throw new RuntimeException(new FileNotFoundException());
    } else {
      file.delete();
    }
  }

  @Override
  @SneakyThrows
  public DocumentStorageKey save(DocumentInfo info, InputStream inputStream) {
    File destFile = new File(rootDirectory, info.getId().getValue().toString());
    FileUtils.copyInputStreamToFile(inputStream, destFile);
    return DocumentStorageKey.from(rootDirectory.toURI().relativize(destFile.toURI()).toString());
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Config {

    private File rootDirectory;

  }
}
