/**
 * 
 */
package io.github.enterprise.web.request;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Sheldon on 2017年12月13日
 *
 */
public class LocalAuthVO {

	@ApiModelProperty(name = "用户名")
	@NotBlank(message = "用户名不能为空")
	private String username;
	
	@ApiModelProperty(name = "密码")
	@NotBlank(message = "密码不能为空")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
