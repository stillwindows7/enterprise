/**
 * 
 */
package io.github.enterprise.web.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Sheldon on 2017年12月14日
 *
 */
public class UserVO {
	
	@ApiModelProperty(name = "用户 id")
	private String id;
	
	@ApiModelProperty(name = "用户昵称")
	private String nickname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
