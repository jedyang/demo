package com.example.yunsheng.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/demo")
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@RequestParam String name)
    {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
        return "Saved.";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers()
    {
        return userRepository.findAll();
    }
}
