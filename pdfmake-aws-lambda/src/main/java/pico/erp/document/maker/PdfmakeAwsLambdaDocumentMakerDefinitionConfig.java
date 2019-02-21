package pico.erp.document.maker;

import com.amazonaws.services.lambda.AWSLambda;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PdfmakeAwsLambdaDocumentMakerDefinitionConfig {

  @NotNull
  String lambdaFunctionName;

  @NotNull
  AWSLambda lambda;

}
