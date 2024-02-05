package it.cagnesgiorgi.swam.elaborato2020.businessLogic.auth;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.cagnesgiorgi.swam.elaborato2020.DAO.UserDAO;
import it.cagnesgiorgi.swam.elaborato2020.domainModel.User;

public class TokenFactory {
	public static final String typ = "JWT";
	public static final String alg = "HmacSHA512";
	private static final String key = "MySuperSecretKeyYOYO242";
	
	public static String generateDigest(String token) {
        Mac sha512Hmac;

        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, alg);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(token.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(macData).toString();
            
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
	}

	private static String generateExpiryDate(){
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 10);
		//calendar.add(Calendar.MINUTE, 1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(timeZone);
		return dateFormat.format(calendar.getTime());
	}

	private static boolean isDateExpired(String date){
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar now = Calendar.getInstance();
		Calendar expiry = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(timeZone);
		try {
			expiry.setTime(dateFormat.parse(date));
			if(now.compareTo(expiry)!=1){
				return false;
			};
		} catch (ParseException e) {
			return true;
		}
		return true;
	}
	
	public static String buildToken(User user){
		String userCountryCode = "";
		String userCountryName = "";
		if(user.getZone()!=null){
			userCountryCode = user.getZone().getCountryCode();
			userCountryName = user.getZone().getCountryName();

		}
		Encoder encoder = Base64.getEncoder();
		String tokenHeader = Json.createObjectBuilder()
                    .add("alg", alg)
                    .add("typ", typ)
                    .build()
                    .toString();
		String tokenHeaderB64 =  encoder.encodeToString(tokenHeader.getBytes());
		String tokenPayload =  Json.createObjectBuilder()
                .add("email", user.getEmail())
                .add("username", user.getUsername())
				.add("userId", user.getId())
				.add("isAdmin", user.hasRole("ADMIN"))
				.add("expiryDate", generateExpiryDate())
                .build()
                .toString();
		String tokenPayloadB64 =  encoder.encodeToString(tokenPayload.getBytes());
		String tokenDigest = generateDigest(tokenHeaderB64 + "." + tokenPayloadB64);
		return tokenHeaderB64 + "." + tokenPayloadB64 + "." + tokenDigest;
		
	}

	public static boolean verifyToken(String token){
		if(token.contains(".")) {
			String[] tokenArray = token.split("\\.");
			String tokenDigest = generateDigest(tokenArray[0] + "." + tokenArray[1]);
			assert tokenDigest != null;
			if(tokenDigest.equals(tokenArray[2])){ //now the token is valid, I have to check the expiry
				byte[] decodedBytes = Base64.getDecoder().decode(tokenArray[1]);
				JsonObject jsonObject = new Gson().fromJson(new String(decodedBytes), JsonObject.class);
				try {
					String expiryDate = jsonObject.get("expiryDate").getAsString();
					return !isDateExpired(expiryDate);
				}catch(NullPointerException exception){
					return false;
				}
			}
		}
		return false;
	}

	public static String getUserMailFrom(String token){
		if(verifyToken(token)){
			String[] tokenArray = token.split("\\.");
			String tokenDigest = generateDigest(tokenArray[0] + "." + tokenArray[1]);
			byte[] decodedBytes = Base64.getDecoder().decode(tokenArray[1]);
			String decodedString = new String(decodedBytes);
			//JsonObject tokenBody = Json.createReader(new StringReader(decodedString)).readObject();
			com.google.gson.JsonObject tokenBody = new Gson().fromJson(decodedString,com.google.gson.JsonObject.class);
			return tokenBody.get("email").getAsString();
		}
		return null;
	}



}
