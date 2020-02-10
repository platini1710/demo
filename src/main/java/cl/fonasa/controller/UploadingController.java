package cl.fonasa.controller;


import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.SftpException;

import cl.fonasa.utils.FTP;




@Controller
@RequestMapping("/*")
public class UploadingController {
	private static final Logger logger = LoggerFactory.getLogger(UploadingController.class);
	 

    @Autowired
    private FTP ftp;

    @RequestMapping(value = "/uploadFile/{path}", method = RequestMethod.POST)
    public String uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles,  @PathVariable("path") String path,   @RequestParam(name = "file") String files) {
    	logger.info("Start");
       	logger.info("root:::" + files);
    	logger.info("path:::" + path);
    	logger.info("uploadingFiles:::" + uploadingFiles.length);
    	String msg="redirect:/";
        try {
        for(MultipartFile uploadedFile : uploadingFiles) {
        	logger.info("nombre archivo:::" + uploadedFile.getOriginalFilename());
            File file = new File( uploadedFile.getOriginalFilename());
            this.ftp.upload(uploadedFile.getInputStream(), path, uploadedFile.getOriginalFilename());
        	logger.info("getAbsolutePath" + file.getAbsolutePath());
        	logger.info("getPath" + file.getPath());
           // uploadedFile.transferTo(file.);
        }
        } catch (Exception  e) {
            // e.printStackTrace();
       
            	 msg="error " + e.getMessage();
         
        }
    	logger.info("fin");
    	return  msg;
    }
	  @RequestMapping(method = RequestMethod.GET, value="/uploadFile/{path}", produces = "application/json")
    public @ResponseBody  String listarFiles(@PathVariable("path") String path) throws SftpException {
			String msg= this.ftp.listFiles(path);
        return msg;
    }
	  
	  @RequestMapping(method = RequestMethod.DELETE, value="/deleteFile/{path}", produces = "application/json")
	    public @ResponseBody  String borrarArchivos(@PathVariable("path") String path,   @RequestParam(name = "file") String file) throws SftpException {
			String msg= this.ftp.deleteFile(path, file);
	        return msg;
	    }
}
