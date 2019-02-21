package pico.erp.document.storage;

import com.amazonaws.services.s3.AmazonS3;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AwsS3DocumentStorageStrategyConfig {

  AmazonS3 amazonS3;

  String amazonS3BucketName;

}
