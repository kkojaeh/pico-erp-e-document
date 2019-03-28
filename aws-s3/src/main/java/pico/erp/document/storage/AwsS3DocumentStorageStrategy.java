package pico.erp.document.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pico.erp.document.DocumentInfo;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AwsS3DocumentStorageStrategy implements DocumentStorageStrategy {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");


  public AwsS3DocumentStorageStrategy(Config config) {
    amazonS3 = config.getAmazonS3();
    amazonS3BucketName = config.getAmazonS3BucketName();
  }

  private final AmazonS3 amazonS3;

  private final String amazonS3BucketName;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Config {

    AmazonS3 amazonS3;

    String amazonS3BucketName;

  }

  private DocumentStorageKey createStorageKey(DocumentInfo info) {
    String key = String
      .format("%s/%s", formatter.format(LocalDateTime.now()), info.getId().getValue());
    return DocumentStorageKey.from(key);
  }

  @Override
  public boolean exists(DocumentStorageKey storageKey) {
    return amazonS3.doesObjectExist(amazonS3BucketName, storageKey.getValue());
  }

  @Override
  public InputStream load(DocumentStorageKey storageKey) {
    S3Object object = amazonS3.getObject(
      new GetObjectRequest(amazonS3BucketName, storageKey.getValue())
    );
    return object.getObjectContent();
  }

  @Override
  public void remove(DocumentStorageKey storageKey) {
    amazonS3.deleteObject(
      new DeleteObjectRequest(amazonS3BucketName, storageKey.getValue())
    );
  }

  @Override
  @SneakyThrows
  public DocumentStorageKey save(DocumentInfo info, InputStream inputStream) {
    DocumentStorageKey storageKey = createStorageKey(info);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(info.getContentLength());
    metadata.setContentType(info.getContentType());
    metadata.setContentDisposition(
      String.format(
        "attachment; filename=\"%s\";",
        URLEncoder.encode(info.getContentName(), "UTF-8").replaceAll("\\+", " ")
      )
    );

    amazonS3.putObject(
      new PutObjectRequest(
        amazonS3BucketName,
        storageKey.getValue(),
        inputStream,
        metadata
      )
    );
    return storageKey;
  }

}
