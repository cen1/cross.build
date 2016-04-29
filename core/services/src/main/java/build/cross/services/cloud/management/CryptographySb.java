package build.cross.services.cloud.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import build.cross.models.jpa.KeyPair;

@Stateless
public class CryptographySb implements CryptographySblocal {

	@PersistenceContext(unitName = "crossbuild")
	private EntityManager em;
	
	private Logger logger = Logger.getLogger(CryptographySb.class.getSimpleName());
	
	@Override
	public String getPublicKeyFromPrivate(String keyName) throws java.io.IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		TypedQuery<build.cross.models.jpa.KeyPair> query = em.createNamedQuery("KeyPair.findByName",
				build.cross.models.jpa.KeyPair.class);
		query.setParameter("name", keyName);

		KeyPair kp = query.getResultList().get(0);
		String pemString = kp.getPrivMaterial();
		
		pemString = pemString.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
		pemString = pemString.replace("-----END RSA PRIVATE KEY-----", "");
		pemString = pemString.replace("\n", "");

		byte[] decoded = Base64.decodeBase64(pemString);
		KeyFactory kf = KeyFactory.getInstance("RSA");

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		PrivateKey privatekey = kf.generatePrivate(keySpec);
		RSAPrivateCrtKey privk = (RSAPrivateCrtKey)privatekey;
		
		//logger.info(new String(Base64.encodeBase64(privk.getEncoded())));
	    
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
	    PublicKey pubKey = kf.generatePublic(publicKeySpec);
	    
	    RSAPublicKey rsaPubKey = (RSAPublicKey)pubKey;
	    String pubString = Base64.encodeBase64String(encodePublicKey(rsaPubKey));
	    
	    logger.info(pubString);
	    
	    return pubString;
	}
	
	@Override
	public String getPublicKeyFromPrivate(KeyPair kp) throws java.io.IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	
		String pemString = kp.getPrivMaterial();
		
		pemString = pemString.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
		pemString = pemString.replace("-----END RSA PRIVATE KEY-----", "");
		pemString = pemString.replace("\n", "");

		byte[] decoded = Base64.decodeBase64(pemString);
		KeyFactory kf = KeyFactory.getInstance("RSA");

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		PrivateKey privatekey = kf.generatePrivate(keySpec);
		RSAPrivateCrtKey privk = (RSAPrivateCrtKey)privatekey;
	    
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
	    PublicKey pubKey = kf.generatePublic(publicKeySpec);
	    
	    RSAPublicKey rsaPubKey = (RSAPublicKey)pubKey;
	    String pubString = Base64.encodeBase64String(encodePublicKey(rsaPubKey));
	    
	    logger.info(pubString);
	    
	    return pubString;
	}
	
	public byte[] encodePublicKey(RSAPublicKey key) throws IOException {
	       ByteArrayOutputStream out = new ByteArrayOutputStream();
	       /* encode the "ssh-rsa" string */
	       byte[] sshrsa = new byte[] {0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a'};
	       out.write(sshrsa);
	       /* Encode the public exponent */
	       BigInteger e = key.getPublicExponent();
	       byte[] data = e.toByteArray();
	       encodeUInt32(data.length, out);
	       out.write(data);
	       /* Encode the modulus */
	       BigInteger m = key.getModulus();
	       data = m.toByteArray();
	       encodeUInt32(data.length, out);
	       out.write(data);
	       return out.toByteArray();
	   }
	
	public void encodeUInt32(int value, OutputStream out) throws IOException {
	       byte[] tmp = new byte[4];
	       tmp[0] = (byte)((value >>> 24) & 0xff);
	       tmp[1] = (byte)((value >>> 16) & 0xff);
	       tmp[2] = (byte)((value >>> 8) & 0xff);
	       tmp[3] = (byte)(value & 0xff);
	       out.write(tmp);
	   }
}
