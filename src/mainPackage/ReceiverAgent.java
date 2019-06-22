package mainPackage;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import behaviours.*;
import guiPackage.ReceiverGUI;

public class ReceiverAgent extends Agent {
	private Logger logger = Logger.getMyLogger(this.getClass().getName());
	private AID sender;
	private ReceiverGUI clientGui;
	private AID[] sendersArray;

	public void registerAgent() throws FIPAException
	{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("client");
		sd.setName("receiverAgent");
		dfd.addServices(sd);
		DFService.register(this, dfd);
	}
	protected void setup() {
		try {
//			DFAgentDescription dfd = new DFAgentDescription();
//			dfd.setName(getAID());
//			ServiceDescription sd = new ServiceDescription();
//			sd.setType("client");
//			sd.setName("chart");
//			dfd.addServices(sd);
//			DFService.register(this, dfd);

			clientGui = new ReceiverGUI(this);

			addBehaviour(new ReceiveFileBehaviour(this, logger, sender));
			addBehaviour(new SendersSearchBehaviour(this, sendersArray, clientGui));

			writeLog("Agentul de tip destinatar:" + getAID().getName() + " a fost lansat cu succes");
			// logger.info("Agentul de tip destinatar:"+getAID().getName()+" a fost lansat
			// cu succes");
		} catch (Exception ex) {
			writeLog("Lansarea agentului " + this.getName() + " a esuat.\n" + ex.getMessage());
			// logger.info("Lansarea agentului " + this.getName() + " a esuat.\n" +
			// ex.getMessage());
			ex.printStackTrace();
			return;
		}
	}

	protected void takeDown() {
		try {
			if (clientGui != null) {
				clientGui.dispose();
			}
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void saveFile(byte[] fileContent, String fileName) {
		try {
			clientGui.saveFile(fileContent, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleUnexpected(ACLMessage msg) {
		if (logger.isLoggable(Logger.WARNING)) {
			logger.log(Logger.WARNING, "Mesaj necunoscut primit de la " + msg.getSender().getName());
			logger.log(Logger.WARNING, "Content is: " + msg.getContent());
		}
	}

	public void handleRefreshSenders() {
		addBehaviour(new SendersSearchBehaviour(this, sendersArray, clientGui));
	}

	public void writeLog(String log) {
		// logger.setUseParentHandlers(false);
		// Create file logger if configuration file level high enough
		if (logger.isLoggable(jade.util.Logger.INFO)) {
			FileHandler fileHandler;
			try {
				fileHandler = new FileHandler("ReceiverLog.txt", true);
//				Handler[] handlers=logger.getHandlers();
			} catch (SecurityException | IOException e) {
				logger.severe(" ReceiverLog.java: FileHandler exception = " + e.getLocalizedMessage());
				e.printStackTrace();
				return;
			}
			fileHandler.setFormatter(new SimpleFormatter());
			fileHandler.setLevel(Level.FINE);
			logger.addHandler(fileHandler);
			logger.info(log);
			fileHandler.close();
		}
	}
}
