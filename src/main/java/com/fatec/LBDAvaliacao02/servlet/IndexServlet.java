package com.fatec.LBDAvaliacao02.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexServlet 
{
	@RequestMapping(name = "index", value = "/index", method = RequestMethod.GET)
	public ModelAndView professorGet(ModelMap model)
	{
		return new ModelAndView("index");
	}
}
