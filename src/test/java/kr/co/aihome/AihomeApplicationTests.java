package kr.co.aihome;

import kr.co.aihome.hello.Hello;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = Hello.class)
class AihomeApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("Hello word 컨트롤러 테스트")
	public void hello_is_returned() throws Exception {
		String hello = "Hello";
		mvc.perform(get("/api/hello"))
				.andExpect(status().isOk())
				.andExpect(content().string("hello spring boot"));
	}

}
