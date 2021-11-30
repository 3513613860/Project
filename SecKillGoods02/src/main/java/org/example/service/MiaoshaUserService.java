package org.example.service;

import org.example.Dao.MiaoshaUserDao;
import org.example.entity.MiaoshaUser;
import org.example.exception.GlobalException;
import org.example.redis.MiaoshaUserKey;
import org.example.redis.RedisService;
import org.example.result.CodeMsg;
import org.example.util.MD5Util;
import org.example.util.UUIDUtil;
import org.example.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    UserService userService;

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){
        //取缓存
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
        if(miaoshaUser != null){
            return miaoshaUser;
        }

        //查数据库
        miaoshaUser =  miaoshaUserDao.getById(id);
        if(miaoshaUser != null){
            redisService.set(MiaoshaUserKey.getById,""+id,miaoshaUser);
        }
        return miaoshaUser;
    }


    public boolean updatePassword(String token,long id,String formPass){
        //取user
        MiaoshaUser user = getById(id);
        if(user == null){
            throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        //处理缓存
        redisService.delete(MiaoshaUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }

    public String login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码
        //直接获取数据库中的密码
        String dbPass = user.getPassword();
        //获取数据库中的salt加盐的部分
        String saltDB = user.getSalt();
        //对用户输入的密码进行加密（第二次进行md5加密）
        String calcPass = MD5Util.formPassToDBPass(formPass,saltDB);
        //判断输入的密码是否与数据库中的密码一致
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();
        System.out.println(token);
        addCookie(response,token,user);
        return token;
    }

    private void addCookie(HttpServletResponse response,String token,MiaoshaUser user){
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
        //延迟有效期
        if(user!=null){
            addCookie(response,token,user);
        }
        return user;
    }
}
