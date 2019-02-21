package pico.erp.document.type;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DocumentTypeDefinition<K, C> {

  C getContext(K key);

  K getKey(String key);

  DocumentTypeId getId();

  String getName();

  @Getter
  @Builder
  @AllArgsConstructor
  class Impl<K, C> implements DocumentTypeDefinition<K, C> {

    DocumentTypeId id;

    String name;

    Function<K, C> contextGetter;

    Function<String, K> keyGetter;

    @Override
    public C getContext(K key) {
      return contextGetter.apply(key);
    }

    @Override
    public K getKey(String key) {
      return keyGetter.apply(key);
    }

  }

}
