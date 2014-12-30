package Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Drivers.CanonicalCommands.CanonCommand;
import Interpreter.State.InterpreterState;

public class ProgramLoader {

	public String fileName_;
	private static ArrayList<LineLoader> lineArray;
	private static ModuleArray moduleArray;
	public static InterpreterState interpreterState;
	public static ArrayList<CanonCommand> hal_commands;
	
	public  ProgramLoader(String fn) throws InterpreterException{
		fileName_ = fn;
		lineArray = new ArrayList<LineLoader>();
		moduleArray = new ModuleArray();
		interpreterState = new InterpreterState();
		hal_commands = new ArrayList<CanonCommand>();
		try{
			InputStream f = new FileInputStream(fileName_);

			BufferedReader inputStream = new BufferedReader(new InputStreamReader(f));
			String line;
			int currentSubNumber = 0;
			int lastModuleNum = -1;
			while ((line = inputStream.readLine()) != null) {
				LineLoader currentBlock = new LineLoader(line);
				lineArray.add(currentBlock); 
				if(currentBlock.isModuleStart()){
					ProgramModule newModule = new ProgramModule(currentBlock.getModuleNum(), lineArray);
					newModule.setStart(lineArray.size() - 1);
				};
				System.out.println(line);
			}
			inputStream.close();
			f.close();
			this.evalute();
		}
		catch (FileNotFoundException e){
		}
		catch (IOException e){
		}
	}

	private void evalute() {
		
		try {
			this.moduleArray.getMain().evalute();
		} catch (InterpreterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
