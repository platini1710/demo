package cl.fonasa.utils;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.tools.FileObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

@Component
public class FTP {

  @Value("${cl.fonasa.sftp.host}")
  private String host;
  @Value("${cl.fonasa.sftp.port}")
  private int port;
  @Value("${cl.fonasa.sftp.user}")
  private String user;
  @Value("${cl.fonasa.sftp.password}")
  private String password;

  private Session session; // JSch Session
  private JSch jsch;
  private Channel channel;
  private ChannelSftp channelSftp;

  private static final Logger log = LoggerFactory.getLogger(FTP.class);

  private FileObject fo;

  public FTP() {
    // context.getInitParameter('urlReal') // connect();
	    log.info("SFTP host to: " + host);

  }

  public FTP(String host, int port, String user, String password) {
	    log.info("FTP user  to: " + user);
	    log.info("FTP host to: " + host);
	    log.info("FTP port to: " + port);

    // context.getInitParameter('urlReal')

    // channelSftp.cd("/fonresuelve"); // TODO: Refactorizar Carpeta defecto
    // connect(host, port, user, password);
    connectSession(host,port,user,password);
    connectChannel();
  }

  /**
   * Conecta Session de JSCH
   */
  public void connectSession(String host, int port, String user, String password) {
    this.jsch = new JSch();
    log.info("SFTP user  to: " + user);
    log.info("SFTP host to: " + host);
    log.info("SFTP port to: " + port);
    try {
      this.session = jsch.getSession(user, host, port);

      this.session.setPassword(password);
      java.util.Properties config = new java.util.Properties();
      config.put("StrictHostKeyChecking", "no");
      this.session.setConfig(config);
      this.session.setPassword(password);
      this.session.connect();
    } catch (JSchException je) {
      log.error(je.getMessage());
      je.printStackTrace();
    }
  }

  public void connect() {
      log.error("connect" + session.isConnected());
    if (!session.isConnected()) {
        log.error("entro isConnected  ");
      connectSession(host,port,user,password);
    }
    if (!channel.isConnected() || !this.channelSftp.isConnected()) {
    	
        log.error("entro connectChannel  ");
      connectChannel();
    }
  }

  public void connectChannel() {
    try {
      this.channel = this.session.openChannel("sftp");
      this.channel.connect();
    } catch (JSchException e) {
      e.printStackTrace();
    }
    this.channelSftp = (ChannelSftp) this.channel;
  }

  public static void main(String[] args) {
    FTP f = new FTP("192.168.1.158", 22, "aespinoza", "platini1710");
    ChannelSftp c = f.getChannelSftp();
    // c.cd("/fonresuelve");
    // println c.pwd();
    // println c.lpwd();
    // println c.getHome();
    String testPath = "/fonresuelve/11/1";
    SftpATTRS attrs;
    try {
      attrs = c.stat(testPath);
      // println "Directorio existe!";

    } catch (SftpException se) {
      // println se.id
      if (se.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
        try {
          c.mkdir(testPath);
        } catch (SftpException e) {
          e.printStackTrace();
        }
        // println("Directorio creado");
        // println c.pwd();
        // Crear archivo
      }
    }

    // println c.ls(c.pwd()).toString()
    // c.disconnect();
    return;
  }

  public ChannelSftp getChannelSftp() {
    return channelSftp;
  }

  public InputStream downloadFile(String path, String file) throws SftpException {
	   
	    FTP f =new  FTP(this.host, this.port, this.user, this.password);
	    ChannelSftp channelSftp = f.getChannelSftp();
    // try{
    try {
      channelSftp.cd("/fonresuelve/" + path);
    } catch (SftpException e) {
      e.printStackTrace();
    }
    // }catch(SftpException se){
    // se.printStackTrace();
    // }
    return channelSftp.get(file);
  }

  public void upload(InputStream isFile, String path, String fileName) {
   
    FTP f =new  FTP(this.host, this.port, this.user, this.password);
    ChannelSftp c = f.getChannelSftp();
    // println fileName;
    // println path;
    path = "/fonresuelve/" + path;
    try {
      c.stat(path);
    } catch (SftpException se) {
      log.warn(se.getMessage());
      se.printStackTrace();
      try {
        c.mkdir(path);
      } catch (SftpException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      log.warn("Error stat ->" + e.getMessage());
    }
    try {
      c.put(isFile, path + "/" + fileName);
    } catch (SftpException e) {
      e.printStackTrace();
    }
  }

  
	public String deleteFile(String path, String file) throws SftpException {
		FTP f = new FTP(this.host, this.port, this.user, this.password);
		ChannelSftp c = f.getChannelSftp();
		boolean deletedflag = false;
		String msg = "archivo borrado";
		// try{
		log.debug(" path to: " + path);
		log.debug(" file to: " + file);
	    path = "/fonresuelve/" + path;
		try {
			c.cd("/" + path);
//	      boolean exist = c.deleteFile("/" + path "/" + file);

			c.rm(file); // This method removes the file from remote server
			deletedflag = true;
			if (deletedflag) {
				log.info("archivo borrado ->");
			}

			else {
				log.info("failed ->");
				msg = "borrado falladp";
			}
		} catch (Exception e) {
			log.error("error  ::"+e.getMessage());
			msg="archivo no existe o " + e.getMessage();

		}
		// }catch(SftpException se){
		// se.printStackTrace();
		// }
		return msg;
	}
	// public String getHost() {
	// return this.ftpHost;
	// }

	public String listFiles(String path) throws SftpException {
		FTP f = new FTP(this.host, this.port, this.user, this.password);
		ChannelSftp c = f.getChannelSftp();
		StringBuffer msg=new StringBuffer();

	    path = "/fonresuelve/" + path;
		log.info(" path to: " + path);
		try {
			c.cd( path);
			log.info(" path to: 1" );
			Vector<LsEntry> filelist = c.ls(path);
			log.info(" path to: 2" );
			for (LsEntry file : filelist) {
				msg.append(file.getFilename() + "\n");
			}

		} catch (Exception e) {
			log.error("error  ::"+e.getMessage());
			msg.append( e.getMessage());

		}

		return msg.toString();
	}
	  // pub
  private void checkConnection() {
    if (!channel.isConnected()) {
      try {
        channel.connect();
      } catch (JSchException e) {
        e.printStackTrace();
      }
    }
  }
  
  
  
  


}