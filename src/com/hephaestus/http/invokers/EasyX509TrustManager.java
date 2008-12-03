package com.hephaestus.http.invokers;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class EasyX509TrustManager implements X509TrustManager {
	
    private X509TrustManager standardTrustManager = null;


	public void checkClientTrusted(X509Certificate[] certificates, String authType)
			throws CertificateException {
        standardTrustManager.checkClientTrusted(certificates,authType);
	}

	public void checkServerTrusted(X509Certificate[] certificates, String authType)
			throws CertificateException {
//        if ((certificates != null) && LOG.isDebugEnabled()) {
//            LOG.debug("Server certificate chain:");
//            for (int i = 0; i < certificates.length; i++) {
//                LOG.debug("X509Certificate[" + i + "]=" + certificates[i]);
//            }
//        }
        if ((certificates != null) && (certificates.length == 1)) {
            certificates[0].checkValidity();
        } else {
            standardTrustManager.checkServerTrusted(certificates,authType);
        }
	}

	public X509Certificate[] getAcceptedIssuers() {
        return this.standardTrustManager.getAcceptedIssuers();
	}

    /**
     * Constructor for EasyX509TrustManager.
     */
    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) {
            throw new NoSuchAlgorithmException("no trust manager found"); //$NON-NLS-1$
        }
        this.standardTrustManager = (X509TrustManager)trustmanagers[0];
    }

}
