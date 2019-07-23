package com.github.icovn.aspect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.login.LoginException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectMethod {
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Before("execution(* com.github.icovn.aop..*(..))")
	public void restrict(JoinPoint joinPoint) throws LoginException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader( System.in) );
		logger.info("\nPlease enter password: ");
		String pass;
		try {
			pass = bufferedReader.readLine();
			if (!"admin".equals(pass)) {
				 throw new LoginException();
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
			throw new LoginException();
		}
	}
	
	@Around("execution(* com.github.icovn.aop..*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		
		Object value = joinPoint.proceed();

		long timeTaken = System.currentTimeMillis() - startTime;

		StringBuilder input = new StringBuilder();
		for (int i = 0; i < codeSignature.getParameterNames().length; i++) {
			if (i != codeSignature.getParameterNames().length - 1) {
				input.append(codeSignature.getParameterNames()[i]).append(":").append(joinPoint.getArgs()[i]).append(", ");
			} else {
				input.append(codeSignature.getParameterNames()[i]).append(":").append(joinPoint.getArgs()[i]);
			}

		}
		logger.info("Method {}  Input:[{}] Ouput:[{}] Time: {} ms.", codeSignature, input, value, timeTaken);
		return value;
	}

}
