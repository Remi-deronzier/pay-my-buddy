package deronzier.remi.payMyBuddyV2.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	private String verificationCode;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		this.verificationCode = request.getParameter("code");
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
