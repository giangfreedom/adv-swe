import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Reposistory {

	public static void main(String[] args) throws IOException {
		//File srcDir = new File("C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/SourceTree");
		//File destDir = new File("C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/Repository/SourceTree/hello.txt/hello.txt");
		String src = "C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/SourceTree";
		String des = "C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/Repository";
		String[] srcNameArray = src.split("/");
		String desNewsrc = des+"/"+srcNameArray[srcNameArray.length - 1];
		System.out.println(desNewsrc);

		String activity = des+"/Activity";
		
		new File(desNewsrc).mkdirs();
		new File(activity).mkdirs();
		
		/*
		 * DATA FOR MANIFEST FILE
		 */
		
		// the version and code name of this project
		String ver = "Version 1 - SWE - SCM project part 1";
				
		// name of the manifest file including date and time
		DateFormat df = new SimpleDateFormat("MM-dd-yy HH-mm");
	    Calendar calobj = Calendar.getInstance();
	    String manifestName = "MANIFEST - "+df.format(calobj.getTime());
	    
		// the command user used
		String userCommand = "CREATE REPOSITORY";
		
		// full source path to original source tree given above
		
		// full source path to repository given above
		
		// the detail for each file
		ArrayList<String> artifacrID = new ArrayList<String>();
		ArrayList<String> fileOriginalName = new ArrayList<String>();
		ArrayList<String> projectTreePath = new ArrayList<String>();
		ArrayList<String> repoPath = new ArrayList<String>();

		
		/*
		 * after create source tree and activity folder I now
		 * create a sub folder inside repo using the file name from
		 * original source tree file. Then inside that sub folder, I copy
		 * the old file of the same name over.
		 */
		
		File folder = new File(src);
		File[] listOfFiles = folder.listFiles();
		//String[] ListofFileinSrc = srcDir.list();
		for(int i = 0; i < listOfFiles.length; i++){
			System.out.println("list of file name: "+listOfFiles[i].getName());
			// C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/Repository/SourceTree = desNewsrc
			new File(desNewsrc+"/"+listOfFiles[i].getName()).mkdirs();

			// now access hello.txt content and call the function that generate 
			// ascii number, get the ascii number them rename the file.
			// get the file content
			String filecontent = readFile(listOfFiles[i]);
			System.out.println("file content is: "+filecontent);
			// get the checksum
			int filechecksum = checksum(filecontent);
			System.out.println("file check sum is: "+filechecksum);
			
			// get the file extention 
			String[] filenameArray = listOfFiles[i].getName().split("\\.");
			String fileExtention = filenameArray[filenameArray.length-1];
			
			// the actual new file name (artifactID 1234.6.txt)
			String newFileName = ""+filechecksum + "." + listOfFiles[i].length() + "." +fileExtention;
			System.out.println("new file name: "+newFileName);
			// rename the file
			File subdir = new File(desNewsrc+"/"+listOfFiles[i].getName()+"/"+newFileName);
			System.out.println("sub dir is: "+subdir);
			//copy the content over
			if(!subdir.exists()){
				Files.copy(listOfFiles[0].toPath(), subdir.toPath());
			}
			
			/*
			 * this section is to store the information for activity file
			 */
			System.out.println("activity section: ");
			artifacrID.add(newFileName);
			fileOriginalName.add(listOfFiles[i].getName());
			projectTreePath.add(listOfFiles[i].getPath());
			repoPath.add(subdir.toPath().toString());
			System.out.println(newFileName);
			System.out.println(listOfFiles[i].getName());
			System.out.println(listOfFiles[i].getPath());
			System.out.println(subdir.toPath().toString());			
		}
		/*
		 * after we finish checking and copy all of the new version of the source tree file
		 * we can now add an activity file.
		 */
		
		/*
		 * combine all of the detail description into 1 full string
		 */
		ArrayList<String> fullDetailDescription = new ArrayList<String>();
		for(int j = 0; j < artifacrID.size(); j++){
			String full = artifacrID.get(j)+"   "+fileOriginalName.get(j)+"   "
					+projectTreePath+ "   "+repoPath;
			fullDetailDescription.add(full);
		}
		// create the manifest file
		try{
		    PrintWriter writer = new PrintWriter(activity+"/"+manifestName+".txt", "UTF-8");
		    writer.println(ver);
		    writer.println();
		    writer.println(manifestName);
		    writer.println();
		    writer.println("User Command: "+userCommand);
		    writer.println();
		    writer.println("Full Source Path: "+src);
		    writer.println();
		    writer.println("Full Destination Path: "+des);
		    writer.println();
		    // list of detail description of added file
		    for(int u = 0; u < fullDetailDescription.size(); u++){
			    writer.println(fullDetailDescription.get(u));
		    }
		    writer.close();
		} catch (IOException e) {
			System.out.println("fail to write manifest file");
		}
		
	}
	/*
	 * this function 
	 * INPUT: is a string that represent the
	 * content of the file
	 * OUTPUT: is an integer that is the sum of all
	 * the character in the content multiple by 1 3 11 17.
	 */
	public static int checksum (String content){
		int S = 0;
		for(int i = 0; i < content.length(); i = i+4){
			S = S + content.charAt(i) * 1;
			if((i+1) <= content.length()-1){
				S = S + content.charAt(i+1) * 3;
			}
			if((i+2) <= content.length()-1){
				S = S + content.charAt(i+2) * 11;
			}
			if((i+3) <= content.length()-1){
				S = S + content.charAt(i+3) * 17;
			}
		}
		return S;
	}
	
	/*
	 * readFile
	 * INPUT: a path that indicate the target file
	 * OUTPUT: a String that represent the content of the file
	 */
	private static String readFile(File file) throws FileNotFoundException {
		SimpleTokenStream read = new SimpleTokenStream(file);
		String s = "";
		while(read.hasNextToken() == true){
			s = s+read.nextToken();
		}	
		return s;
	}
	
	/*
	 * readFilePath
	 * INPUT: a path that indicate the target file
	 * OUTPUT: a String that represent the content of the file
	 */
	/*
	private static String readFilePath(Path file) throws FileNotFoundException {
		File readfile = file.toFile();
		SimpleTokenStream read = new SimpleTokenStream(readfile);
		String s = "";
		while(read.hasNextToken() == true){
			s = s+read.nextToken();
		}		
		return s;
	}
	*/
}
