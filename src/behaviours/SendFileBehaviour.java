package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import mainPackage.ReceiverAgent;
import mainPackage.SenderAgent;

public class SendFileBehaviour extends OneShotBehaviour{
	private AID receiver;
	private byte[] fileContent;
	private String fileName;
	private Logger logger;
	private ACLMessage sentMsg;

	public SendFileBehaviour(Agent a, byte[] refFileContent, AID refReceiver, String refFileName,Logger refLogger) {
		super(a);
		this.logger=refLogger;
		receiver = refReceiver;
		this.fileContent = refFileContent;
		this.fileName = refFileName;
	}

	public void action() {
		sentMsg = new ACLMessage();
		sentMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_RECRUITING);
		sentMsg = new ACLMessage(ACLMessage.PROPOSE);
		sentMsg.setConversationId(myAgent.getName());
		
		sentMsg.clearAllReceiver();
		sentMsg.addReceiver(receiver);
		sentMsg.setByteSequenceContent(fileContent);
		// added
		sentMsg.addUserDefinedParameter("file-name", fileName);

		// spokenMsg.setContent(sentence);
		myAgent.send(sentMsg);
		logger.log(Logger.INFO, "Fisier trimis.");
//		logger.info("Agentul " + myAgent.getName() + "a trimis fisierul:" + fileName + " catre agentul "
//				+ receiver.getName());
		
		((SenderAgent) myAgent).writeLog("Agentul " + myAgent.getName() + "a trimis fisierul:" + fileName + " catre agentul "
				+ receiver.getName());
	}
}

