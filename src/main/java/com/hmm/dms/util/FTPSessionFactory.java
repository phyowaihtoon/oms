package com.hmm.dms.util;

import com.hmm.dms.config.FTPConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Component;

@Component
public class FTPSessionFactory {

    @Autowired
    private FTPConfiguration ftpConfig;

    public DefaultFtpSessionFactory getFactory() {
        DefaultFtpSessionFactory dsf = new DefaultFtpSessionFactory();
        dsf.setHost(ftpConfig.getHost());
        dsf.setPort(ftpConfig.getPort());
        dsf.setUsername(ftpConfig.getUsername());
        dsf.setPassword(ftpConfig.getPassword());
        dsf.setControlEncoding("UTF-8");
        return dsf;
    }

    public FtpSession getSession() {
        return getFactory().getSession();
    }
}
