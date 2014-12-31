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
			ProgramModule lastModule = null;
			boolean programEndReached = false;
			while (((line = inputStream.readLine()) != null)&&(!programEndReached)) {
				LineLoader currentBlock = new LineLoader(line);
				lineArray.add(currentBlock); 
				final int lineOrdinalNum = lineArray.size() - 1;
				if(currentBlock.isModuleStart()){
					ProgramModule newModule = new ProgramModule(currentBlock.getModuleNum(), lineArray);
					newModule.setStart(lineOrdinalNum);
					moduleArray.add(newModule);
					lastModule = newModule;
				};
				if(currentBlock.isProgramEnd()){
					programEndReached = true;
					if(lastModule == null){
						ProgramModule newModule = new ProgramModule(1, lineArray);
						newModule.setStart(0);
						moduleArray.add(newModule);
						lastModule = newModule;
					};
					lastModule.setEnd(lineOrdinalNum);
				}
				System.out.println(line);
			}
			inputStream.close();
			f.close();
			if(!programEndReached) throw new InterpreterException("M2 needed in the end of program!");
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
