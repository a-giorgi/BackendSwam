package it.cagnesgiorgi.swam.elaborato2020.businessLogic.DTOs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginRequest {
	@XmlElement 
	public String username;
	@XmlElement
	public String password;
}
