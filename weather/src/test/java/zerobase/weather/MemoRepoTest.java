package zerobase.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import zerobase.weather.domain.Memo;
import zerobase.weather.repo.JDBCMemoRepo;

@SpringBootTest
@Transactional
public class MemoRepoTest {
    @Autowired
    JDBCMemoRepo memoRepo;

    @Test
    void insertMemoTest() {
	// given
	Memo newMemo = new Memo(2, "this is new memo2");

	// when
	memoRepo.save(newMemo);

	// then
	var result = memoRepo.findById(2);
	assertEquals(result.get().getText(), "this is new memo2");
    }

    @Test
    void findAllMemoTest() {
	var memoList = memoRepo.findAll();
	System.out.println(memoList);
	assertNotNull(memoList);
    }
}
