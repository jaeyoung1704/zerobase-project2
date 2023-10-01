package zerobase.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import zerobase.weather.domain.Memo;
import zerobase.weather.repo.JpaMemoRepo;

@SpringBootTest
//테스트에 transactional 붙히면 항상 롤백
@Transactional
public class JpaMemoRepoTest {

    @Autowired
    JpaMemoRepo jpaMemoRepo;

    @Test
    void insertMemoTest() {
	// given
	Memo newMemo = new Memo(10, "this is jpa memo");
	// when
	jpaMemoRepo.save(newMemo);
	// then
	var memoList = jpaMemoRepo.findAll();
	assertTrue(!memoList.isEmpty());
    }

    @Test
    void findByItdTest() {
	// given
	Memo newMemo = new Memo(11, "jpa");
	// when
	jpaMemoRepo.save(newMemo);
	System.out.println(newMemo.getId());
	// then
	var result = jpaMemoRepo.findById(newMemo.getId());
	assertEquals(result.get().getText(), "jpa");
    }
}
