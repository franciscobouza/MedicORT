package controllers;

import java.io.File;

import play.mvc.Controller;
import play.mvc.Result;

public class FileService extends Controller {
   public static Result getFile(String file){
          File myfile = new File(System.getProperty("user.dir")+"/profesional/"+file);
          myfile.getParentFile().mkdirs();
          return ok(myfile);
   }
}