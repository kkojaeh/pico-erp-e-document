package pico.erp.document.subject;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public interface DocumentSubjectDefinition<K, C> {

  C getContext(K key);

  K getKey(String key);

  DocumentSubjectId getId();

  String getName();

  @Getter
  @Builder
  @AllArgsConstructor
  class Impl<K, C> implements DocumentSubjectDefinition<K, C> {

    DocumentSubjectId id;

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
