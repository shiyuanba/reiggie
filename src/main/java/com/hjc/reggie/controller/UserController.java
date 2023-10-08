package com.hjc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjc.reggie.common.R;
import com.hjc.reggie.entity.User;
import com.hjc.reggie.service.UserService;
import com.hjc.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if (phone != null) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            session.setAttribute(phone,code);
            return R.success("发生成功！");
        }
        return R.error("发送失败！");
    }

    /**
     * 移动端登入和注册账号
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        String phone = (String) map.get("phone");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }

        session.setAttribute("user",user.getId());
        return R.success(user);
    }

    /**
     * 移动端退出当前账号
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
