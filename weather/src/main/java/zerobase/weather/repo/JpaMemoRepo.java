package zerobase.weather.repo;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import zerobase.weather.domain.Memo;

//@Repository
public interface JpaMemoRepo extends JpaRepository<Memo, Integer> {
}
