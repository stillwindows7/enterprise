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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.enterprise.model.User;
import io.github.enterprise.service.AccessTokenService;
import io.github.enterprise.service.AuthService;
import io.github.enterprise.utils.common.Assert;
import io.github.enterprise.utils.common.JsonResult;
import io.github.enterprise.utils.common.MapperUtils;
import io.github.enterprise.web.request.LocalAuthVO;
import io.github.enterprise.web.response.UserVO;
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
	private MapperUtils mapperUtils;
	
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
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "查询 access token 是否过期", notes = "查询 access token 是否过期")
	@GetMapping(value = "/accesstoken/check/{accessToken}")
	public JsonResult<Boolean> checkAccessToken(@PathVariable String accessToken) {
		Boolean result = this.accessTokenService.check(accessToken);
		return JsonResult.getSuccessResult(result, "查询成功");
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据 access token 查询用户", notes = "根据 access token 查询用户")
	@GetMapping(value = "/accesstoken/query/{accessToken}")
	public JsonResult<UserVO> queryUserByAccessToken(@PathVariable String accessToken) {
		logger.debug("access token: {}", accessToken);
		
		JsonResult<UserVO> jsonResult = null;
		Optional<User> userOptional = this.accessTokenService.findByAccessToken(accessToken);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			UserVO userVO = this.mapperUtils.map(user, UserVO.class);
			jsonResult = JsonResult.getSuccessResult(userVO, "查询成功");
		} else {
			jsonResult = JsonResult.getFailResult("查询失败");
		}
		return jsonResult;
	}
	
}
