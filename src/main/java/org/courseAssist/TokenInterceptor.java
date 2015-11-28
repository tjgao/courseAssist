package org.courseAssist;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TokenInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		final String authHeader = arg0.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.info("Missing or invalid authorization header!");
			return false;
		}
		final String token = authHeader.substring(7);
        try {
            final Claims claims = Jwts.parser().setSigningKey(AppConfig.signingKey)
                .parseClaimsJws(token).getBody();
            arg0.setAttribute("uid", claims.getSubject());
        }
        catch (final SignatureException e) {
            logger.info("Invalid token.");
            return false;
        }		
		return true;
	}

}
