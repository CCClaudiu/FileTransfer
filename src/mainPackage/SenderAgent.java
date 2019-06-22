package mainPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import behaviours.ReceiversSearchBehaviour;
import behaviours.SendFileBehaviour;
import guiPackage.SenderGUI;
import jade.content.abs.AbsAggregate;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsTerm;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.DeadAgent;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import jade.util.Logger;

//import ontology.FileTransferOntology;

import java.util.Iterator;

//to do:
//change log file paths
//validari
//testare
//documentatie
//imbunatatire cod
//implementare take down
public class SenderAgent extends Agent {
	private Logger logger = Logger.getMyLogger(this.getClass().getName());
	private AID[] receivers;
	private SenderGUI senderGui;

	protected void setup() {
		try {
			// console prints for testing(test if logging file exists inside the project)
			System.out.println(getClass().getClassLoader().getResource("logging.properties"));
			System.out.println(SenderAgent.class.getClassLoader().getResource("logging.properties"));

			// set agent's description and init it's service type
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("sender");
			sd.setName("SenderAgent");
			dfd.addServices(sd);

			DFService.register(this, dfd);

			// init receivers array and sender's gui
			receivers = new AID[0];
			senderGui = new SenderGUI(receivers, this);

			// start up behaviours region
			// search for receivers behaviour
			addBehaviour(new ReceiversSearchBehaviour(this, receivers, senderGui));

			writeLog("Agentul de tip emitator:" + getAID().getName() + " a fost lansat cu succes");
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
			if (senderGui != null) {
				senderGui.dispose();
			}
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// logger.info("Agentul " + this.getName() + " a fost dealocat.");
		writeLog("Agentul " + this.getName() + " a fost dealocat.");
	}

	public void handleSendFile(byte[] fileContent, AID receiver, String fileName) {
		addBehaviour(new SendFileBehaviour(this, fileContent, receiver, fileName, logger));
	}

	public void writeLog(String log) {
		// logger.setUseParentHandlers(false);
		// Create file logger if configuration file level high enough
		if (logger.isLoggable(jade.util.Logger.INFO)) {
			FileHandler fileHandler;
			try {
				fileHandler = new FileHandler("SenderLog.txt", true);
//				Handler[] handlers=logger.getHandlers();
			} catch (SecurityException | IOException e) {
				logger.severe(" SenderAgent.java: FileHandler exception = " + e.getLocalizedMessage());
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
