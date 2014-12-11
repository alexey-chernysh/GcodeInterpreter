package CNCProgram;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Exceptions.GcodeRuntimeException;
import Exceptions.LexerException;

public class CNCProgram {

	private String fileName_;
	public static ArrayList<CNCProgramModule> sub_program_list = new ArrayList<CNCProgramModule>();
	
	public  CNCProgram(String fn) throws LexerException, GcodeRuntimeException{
		fileName_ = fn;
		int stringCounter = 0;
		try{
			InputStream f = new FileInputStream(fileName_);

			BufferedReader inputStream = new BufferedReader(new InputStreamReader(f));
			String line;
			CNCProgramModule current_program = new CNCProgramModule(0);
			sub_program_list.add(current_program);
			while ((line = inputStream.readLine()) != null) {
				CNCProgramFrame currentBlock = new CNCProgramFrame(line);
				int tmp = currentBlock.getProgramNum();
				if(tmp == 0) { 
					current_program.add(currentBlock); 
				}
				else {
					current_program = new CNCProgramModule(tmp);
					sub_program_list.add(current_program);
					current_program.add(currentBlock); 
				}
				System.out.println(line);
				stringCounter++;
			}
			inputStream.close();
			f.close();
			this.execute();
		}
		catch (FileNotFoundException e){
		}
		catch (IOException e){
		}
	}

}
