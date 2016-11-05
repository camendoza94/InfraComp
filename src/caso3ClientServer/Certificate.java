package caso3ClientServer;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Date;

import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

@SuppressWarnings("deprecation")
public class Certificate {

	private X509Certificate cert;
	private KeyPair keyPair;

	public Certificate() {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
			keyPairGenerator.initialize(1024);
			keyPair = keyPairGenerator.generateKeyPair();
			PublicKey pubKey = keyPair.getPublic();
	        PublicKey pubKey2 = keyPair.getPublic();
	        PrivateKey privKey = keyPair.getPrivate();
	        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
	        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
	        X500Name certName = new X500Name("CN=Infracomp, OU=None, O=None, L=None, C=None");
			Date startDate = new Date();
			Date expiryDate = new Date(System.currentTimeMillis() + 31536000000L); 
	        X500Name certName2 = new X500Name("CN=Infracomp, OU=None, O=None, L=None, C=None");
	        JcaX509v3CertificateBuilder v3CertGen = new JcaX509v3CertificateBuilder(certName, serialNumber, startDate, expiryDate, certName2 , pubKey);
	        v3CertGen.addExtension(X509Extension.subjectKeyIdentifier, false, (ASN1Encodable)extUtils.createSubjectKeyIdentifier(pubKey));
	        v3CertGen.addExtension(X509Extension.authorityKeyIdentifier, false, (ASN1Encodable)extUtils.createAuthorityKeyIdentifier(pubKey2));
	        setCert(new JcaX509CertificateConverter().setProvider("BC").getCertificate(v3CertGen.build(new JcaContentSignerBuilder("MD5withRSA").setProvider("BC").build(privKey))));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}
	

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

}
