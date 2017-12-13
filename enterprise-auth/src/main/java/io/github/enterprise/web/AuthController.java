/**
 * 
 */
package io.github.enterprise.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.github.enterprise.model.User;
import io.github.enterprise.service.AccessTokenService;
import io.github.enterprise.service.AuthService;
import io.github.enterprise.utils.common.Assert;
import io.github.enterprise.utils.common.JsonResult;
import io.github.enterprise.web.req.LocalAuthVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 身份验证 API
 * 
 * Created by Sheldon on 2017年12月13日
 *
 */
@Api(value = "/auth", tags = "身份验证 API")
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	private static Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AccessTokenService accessTokenService;
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "用户登录接口", notes = "使用账号和密码进行登录")    
    @PostMapping(value = "/login")
	public JsonResult<String> login(@Valid @RequestBody LocalAuthVO localAuthVo, @ApiIgnore BindingResult errors, HttpServletRequest request) {
		Assert.newErrorsProcess(errors);
		
		JsonResult<String> jsonResult = null;
		Optional<User> userOptional = this.authService.login(localAuthVo.getUsername(), localAuthVo.getPassword());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String accessToken = this.accessTokenService.generateAccessToken(user);
			logger.debug("access token: {}", accessToken);
			
			jsonResult = JsonResult.getSuccessResult(accessToken, "登录成功");
		} else {
			jsonResult = JsonResult.getFailResult("账号或者密码错误");
		}
		
		return jsonResult;
	}
	
}
