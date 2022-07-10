package test.service;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface Repository extends ReactiveCrudRepository<DbData, String> {
}
