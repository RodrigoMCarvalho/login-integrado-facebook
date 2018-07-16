package br.com.sociallogin.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;

@Named
@SessionScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private SocialAuthManager socialManager;
	private Profile profile;

	private final String mainURL = "http://localhost:8080/SocialLogin/home.xhtml";
	private final String redirectURL = "http://localhost:8080/SocialLogin/redirectHome.xhtml";
	private final String provider = "facebook";

	public void conectar() {
		Properties prop = System.getProperties();
		prop.put("graph.facebook.com.consumer_key", "210940952956553"); // ID do Aplicativo
		prop.put("graph.facebook.com.consumer_secret", "0f73ef2fc44d277c87b10c0081fe7334"); // Chave Secreta do
																							// Aplicativo
		prop.put("graph.facebook.com.custom_permissions", "public_profile, email");

		SocialAuthConfig socialConfig = SocialAuthConfig.getDefault();

		try {
			socialConfig.load(prop);
			socialManager = new SocialAuthManager();
			socialManager.setSocialAuthConfig(socialConfig);
			String URLRetorno = socialManager.getAuthenticationUrl(provider, redirectURL);
			FacesContext.getCurrentInstance().getExternalContext().redirect(URLRetorno);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPerfilUsuario() throws Exception {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		Map<String, String> parametros = SocialAuthUtil.getRequestParametersMap(request);
		
		if (socialManager != null) {
			AuthProvider provider = socialManager.connect(parametros);
			this.setProfile(provider.getUserProfile());
		}
		FacesContext.getCurrentInstance().getExternalContext().redirect(mainURL);
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	

}
