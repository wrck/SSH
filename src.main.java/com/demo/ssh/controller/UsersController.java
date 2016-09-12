package com.demo.ssh.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.demo.ssh.dao.UsersRepository;
import com.demo.ssh.entity.Users;
import com.demo.ssh.service.UsersService;

@Controller
@RequestMapping("/user")
public class UsersController {
	@Autowired
	@Resource(name = "usersService")
	private UsersService service;

	@Autowired
	private UsersRepository usersRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView findOne(@PathVariable(value = "id") long id, com.demo.ssh.view.ModelAndView mv) {
		Users users = usersRepository.findOne(id);
		mv.addObject("model", users);
		mv.setViewName("user-detail");
		return mv;
	}

	@RequestMapping
	public ModelAndView find(ModelAndView mv, HttpServletRequest request) {
		Set<Entry<String, String[]>> params = request.getParameterMap().entrySet();
		String sqlParams = "";
		int i = 1;
		for (Entry<String, String[]> entry : params) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			if (value == null || value.isEmpty()) {
				continue;
			}

			if (i == 1) {
				if (value.contains("%"))
					sqlParams += key + " like '" + value + "'";
				else
					sqlParams += key + "='" + value + "'";
			} else {
				if (value.contains("%"))
					sqlParams += " and " + key + " like '" + value + "'";
				else
					sqlParams += " and " + key + "='" + value + "'";
			}
			i++;

		}
		List<Users> users = usersRepository.find(sqlParams);
		mv.addObject("model", users);
		mv.setViewName("/users");
		return mv;
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public ModelAndView count() {
		List<Users> users = service.getAllUser();
		ModelAndView mv = new ModelAndView();
		mv.addObject("count", users.size());
		mv.addObject("model", users);
		mv.setViewName("/users");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(ModelAndView mv) {
		// String uri = request.getRequestURI();
		// int start = uri.lastIndexOf("/");
		// String path = uri.substring(start);
		// mv.addObject("requestUri", path);
		mv.setViewName("user-detail");
		return mv;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(Users user, Map<String, String> mv) {
		long id = (long) usersRepository.update(user);
		return "redirect:/user";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String create(Users user, Map<String, String> mv) {
		long id = (long) usersRepository.create(user);
		return "redirect:/user";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(Users user, Map<String, String> mv) {
		usersRepository.delete(user);
		return "redirect:/user";
	}
}