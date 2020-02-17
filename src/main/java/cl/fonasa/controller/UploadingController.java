package cl.fonasa.controller;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @RequestMapping(value = "/uploadFile/{path}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String  uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles,  @PathVariable("path") String path,   @RequestParam(name = "file") String files) {
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
    	logger.info("fin");
    	 return msg;
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
	  
	  
	  @RequestMapping(method = RequestMethod.GET, value="/downloadFile/{appFolder}")
	    @ResponseBody
	    public void downloadGenerico(@PathVariable String appFolder, HttpServletRequest httpRequest,
	            HttpServletResponse response, @RequestParam(name = "file") String file) {
			String msg= "ok";
	    	logger.debug("file ::" +file);
	        try {
	            // TODO: Cambiar metodo downloadFile, tiene en duro carpeta 'fonresuelve'
	            InputStream is = this.ftp.downloadFile(appFolder, file);
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");
	            String type = httpRequest.getServletContext().getMimeType(file);
	            response.setHeader("Content-Type", type);
	            int read = 0;
	            byte[] bytes = new byte[4096];
	            OutputStream os = null;
	            os = response.getOutputStream();
	            while ((read = is.read(bytes)) != -1) {
	                os.write(bytes, 0, read);
	            }
	            os.flush();
	            os.close();
	        } catch (IOException | SftpException e) {
	            // e.printStackTrace();
	        	msg="Documento no encontrado";
	            try {
	                response.sendError(HttpStatus.NOT_FOUND.value(), "Documento no encontrado");
	            } catch (IOException ex) {
	    	    	logger.error(e.getMessage(),e);
	            }
	        }
	
	    }
}
