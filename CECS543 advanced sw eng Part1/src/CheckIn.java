import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CheckIn {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//user checked out files 
		// source folder contain hello.txt FOLDER which contain ####.#.txt files
		String src = "C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/UserCheckedOutFiles/SourceTree/hello.txt";

		// repository folder contain SourceTree which contain hello.txt FOLDER which contain ####.#.txt files
		String des = "C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/Repository/SourceTree/hello.txt";
		
		// getfile take in src and des path and move the file that got modify
		// then return an arraylist of detail description on each of the file
		// this array list will be pass to manifest function to build the last part of manifest file.
		ArrayList<String> fullDescription = getFile(src,des);
		
		String activityPath = "C:/Users/SuperAdmin/Desktop/cecs 543 adv swe/Repository/Activity";
		
		manifest(fullDescription,src,des,activityPath);
	}
	
	/*
	 * this function take in:
	 * 
	 * fullDescription : the last line of description in the manifest file
	 * src : the source where the file come from
	 * des : the location where the file get move to
	 * activity : the path indicate where the file will be written.
	 */
	static void manifest(ArrayList<String> fullDescription,String src, String des, String activity) {
		/*
		 * DATA FOR MANIFEST FILE
		 */
		
		// the version and code name of this project
		String ver = "Version 2 - SWE - SCM project part 2";
				
		// name of the manifest file including date and time
		DateFormat df = new SimpleDateFormat("MM-dd-yy HH-mm");
	    Calendar calobj = Calendar.getInstance();
	    String manifestName = "MANIFEST - "+df.format(calobj.getTime());
	    
		// the command user used
		String userCommand = "CHECK IN";
		
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
		    
		    // no file got change
		    if(fullDescription.isEmpty()){
		    	writer.println("None of the check in file got any change. ");
		    }
		    
		    // some files got change
		    for(int u = 0; u < fullDescription.size(); u++){
			    writer.println(fullDescription.get(u));
		    }
		    writer.close();
		} catch (IOException e) {
			System.out.println("fail to write manifest file");
		}
		
	}
	
	
	/*
	 * this function take in 
	 * src : the source path of the folder that contain the files we need to check in
	 * des : the destination path to the folder that we want to check in
	 * 
	 * This function check the file in and for every file that got checked in, a
	 * this function will store the information of that file in the 4 array lists
	 * and concatenate them into 1 item in fullDetailDescription array list.
	 * and RETURN the fullDetailDescription array list.
	 */
	static ArrayList<String> getFile(String src, String des) throws IOException{

		// the detail for each file manifest
		ArrayList<String> artifacrID = new ArrayList<String>();
		ArrayList<String> fileOriginalName = new ArrayList<String>();
		ArrayList<String> projectTreePath = new ArrayList<String>();
		ArrayList<String> repoPath = new ArrayList<String>();
		
        File folder = new File(src);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++){
        	System.out.println("list of file name: "+listOfFiles[i].getName());
	
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


        	// create a file object using the destination path append with file name and extention.
        	File newfile = new File(des+"/"+newFileName);
        	System.out.println("new file is: "+newfile);
        	//check is the file exist in the destination folder
        	// if it is not then copy the content over
        	if(!newfile.exists()){
        		Files.copy(listOfFiles[i].toPath(), newfile.toPath());
        		
        		/*
    			 * this section is to store the information for activity file
    			 * We create new activity file only for file that got change
    			 */
    			System.out.println("activity section: ");
    			artifacrID.add(newFileName);
    			fileOriginalName.add(listOfFiles[i].getName());
    			projectTreePath.add(listOfFiles[i].getPath());
    			repoPath.add(newfile.toPath().toString());
    			System.out.println(newFileName);
    			System.out.println(listOfFiles[i].getName());
    			System.out.println(listOfFiles[i].getPath());
    			System.out.println(newfile.toPath().toString());
        	}
        	
        }
        
		/*
		 * combine all of the detail description into 1 full string
		 */
		ArrayList<String> fullDetailDescription = new ArrayList<String>();
		for(int j = 0; j < artifacrID.size(); j++){
			String full = artifacrID.get(j)+"   "+fileOriginalName.get(j)+"   "
					+projectTreePath+ "   "+repoPath;
			fullDetailDescription.add(full);
		}
		
		return fullDetailDescription;
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

}
