package cph.controller;

import cph.domain.Role;
import cph.domain.UserInfo;
import cph.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersController {


    @Autowired
    private IUsersService usersService;


    @RequestMapping("/findAll.do")
    public ModelAndView findAll(){
        ModelAndView modelAndView = new ModelAndView();

        List<UserInfo> userInfos = usersService.findAll();

        modelAndView.addObject("userList",userInfos);
        modelAndView.setViewName("user-list");

        return modelAndView;
    }

    @RequestMapping("/save.do")
    public String save(UserInfo userInfo) throws Exception {
        usersService.save(userInfo);
        return "redirect:findAll.do";

    }



    @RequestMapping("/findById.do")
    public ModelAndView findById(String id) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        UserInfo userInfo = usersService.findById(id);
        modelAndView.addObject("user",userInfo);
        modelAndView.setViewName("user-show");
        return modelAndView;
    }


    //根据id查询用户并查询可以添加的角色
    @RequestMapping("/findUserByIdAndAllRole.do")
    public ModelAndView findUserByIdAndAllRole(@RequestParam(name = "id",required = true) String userid) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        //1.根据用户查询用户
        UserInfo userInfo = usersService.findById(userid);
        //2.根据用户id查询可以添加的角色
        List<Role> otherRoles = usersService.findOtherRoles(userid);
        modelAndView.addObject("user",userInfo);
        modelAndView.addObject("roleList",otherRoles);
        modelAndView.setViewName("user-role-add");
        return modelAndView;
   }


   //给用户添加角色
    @RequestMapping("/addRoleToUser.do")
    public String addRoleToUser(@RequestParam(name = "userId",required = true)String userId,@RequestParam(name = "ids",required = true) String[] roleIds) throws Exception{
        usersService.addRoleToUser(userId,roleIds);
        return "redirect:findAll.do";
    }

}
