package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import mainPackage.ReceiverAgent;

public class ReceiveFileBehaviour extends CyclicBehaviour {
	private MessageTemplate template;
	private Logger logger;
	private AID sender;

	public ReceiveFileBehaviour(Agent a, Logger refLogger, AID refSender) {
		super(a);
		this.logger = refLogger;
		this.sender = refSender;
	}

	public void action() {
		if (sender != null ) {
			template = MessageTemplate.MatchConversationId(sender.getName());
		}
		ACLMessage msg = myAgent.receive(template);
		//ACLMessage msg = myAgent.receive();
		if (msg != null) {
			if (msg.getPerformative() == ACLMessage.PROPOSE) {
				String fileName = msg.getUserDefinedParameter("file-name");
				byte[] fileContent = msg.getByteSequenceContent();
				((ReceiverAgent) myAgent).saveFile(fileContent, fileName);
				// System.out.println(msg.getContent());
				((ReceiverAgent) myAgent).writeLog("Agentul " + myAgent.getName() + " a primit fisierul cu denumirea " + fileName
						+ " de la agentul " + msg.getSender().getName());
//				logger.info("Agentul " + myAgent.getName() + " a primit fisierul cu denumirea " + fileName
//						+ " de la agentul " + msg.getSender().getName());
			} else if (msg.getPerformative() == ACLMessage.INFORM) {
				System.out.println("Informare de la DF.");
			} else {
				((ReceiverAgent) myAgent).handleUnexpected(msg);
			}
		} else {
			block();
		}
	}
} 