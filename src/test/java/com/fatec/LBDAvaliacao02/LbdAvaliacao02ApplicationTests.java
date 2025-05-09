package com.fatec.LBDAvaliacao02;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ModelMap;

import com.fatec.LBDAvaliacao02.servlet.AlunoServlet;

@SpringBootTest
class LbdAvaliacao02ApplicationTests {
	@Autowired
	private AlunoServlet AlunoServlet;
	@Test
	void contextLoads() {
		ModelMap modelMap = new ModelMap();
		AlunoServlet.alunoGet(modelMap);
	}

}
