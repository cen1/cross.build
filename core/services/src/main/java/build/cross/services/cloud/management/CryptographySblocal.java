package build.cross.services.cloud.management;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ejb.Local;

import build.cross.models.jpa.KeyPair;

@Local
public interface CryptographySblocal {

	String getPublicKeyFromPrivate(String keyName)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

	String getPublicKeyFromPrivate(KeyPair kp) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

}
