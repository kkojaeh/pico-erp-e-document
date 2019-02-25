package pico.erp.document.context;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import pico.erp.shared.data.ContentInputStream;

public class DocumentContextFactoryImpl implements DocumentContextFactory {

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
    .ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  private static String dateFormatter(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    return dateFormatter.format(OffsetDateTime.parse(value));
  }

  private static String dateTimeFormatter(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    return dateTimeFormatter.format(OffsetDateTime.parse(value));
  }

  private static Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

  @SneakyThrows
  private static String phoneNumberFormatter(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    PhoneNumber number = phoneNumberUtil.parse(value, getLocale().getCountry());
    return String.format("(+%d) %s", number.getCountryCode(),
      phoneNumberUtil.format(number, PhoneNumberFormat.NATIONAL));
  }

  @SneakyThrows
  private static String contentEncoder(ContentInputStream value) {
    StringBuilder builder = new StringBuilder();
    builder.append("data:");
    builder.append(value.getContentType());
    builder.append(";base64,");
    byte[] bytes = IOUtils.readFully(value, (int) value.getContentLength());
    builder.append(DatatypeConverter.printBase64Binary(bytes));
    return builder.toString();
  }

  @Override
  public DocumentContext factory() {
    return DocumentContext.builder()
      .dateFormatter(DocumentContextFactoryImpl::dateFormatter)
      .dateTimeFormatter(DocumentContextFactoryImpl::dateTimeFormatter)
      .phoneNumberFormatter(DocumentContextFactoryImpl::phoneNumberFormatter)
      .contentEncoder(DocumentContextFactoryImpl::contentEncoder)
      .build();
  }

}
