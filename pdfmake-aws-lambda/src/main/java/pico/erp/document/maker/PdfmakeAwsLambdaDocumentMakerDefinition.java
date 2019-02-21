package pico.erp.document.maker;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import pico.erp.document.template.DocumentTemplate;
import pico.erp.shared.data.ContentInputStream;

public class PdfmakeAwsLambdaDocumentMakerDefinition implements DocumentMakerDefinition {

  private final AWSLambda awsLambda;

  private final String awsLambdaFunctionName;

  public PdfmakeAwsLambdaDocumentMakerDefinition(
    PdfmakeAwsLambdaDocumentMakerDefinitionConfig config) {
    awsLambda = config.getAwsLambda();
    awsLambdaFunctionName = config.getAwsLambdaFunctionName();
  }

  @Override
  public ContentInputStream make(String name, DocumentTemplate template) {
    InvokeRequest request = new InvokeRequest().withFunctionName(awsLambdaFunctionName)
      .withPayload(ByteBuffer.wrap(template.asString().getBytes()));

    InvokeResult result = awsLambda.invoke(request);
    byte[] base64bytes = result.getPayload().array();
    // json 형태의 시작/마지막 (") 를 제거
    byte[] withoutQuotes = Arrays.copyOfRange(base64bytes, 1, base64bytes.length - 1);
    byte[] bytes = Base64.getDecoder().decode(withoutQuotes);
    return ContentInputStream.builder()
      .name(name + ".pdf")
      .inputStream(new ByteArrayInputStream(bytes))
      .contentType("application/pdf")
      .contentLength(bytes.length)
      .build();
  }
}
