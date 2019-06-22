package behaviours;

import guiPackage.ReceiverGUI;
import guiPackage.SenderGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import mainPackage.SenderAgent;

public class ReceiversSearchBehaviour extends CyclicBehaviour{
	private DFAgentDescription dfTemplate;
	private AID[] receivers;
	private SenderGUI senderGui;
	private int lastSize=-1;
	
	public ReceiversSearchBehaviour(Agent a,AID[] refReceivers,SenderGUI refSenderGui) {
		super(a);
		this.receivers=refReceivers;
		this.senderGui=refSenderGui;
		dfTemplate = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("client");
		dfTemplate.addServices(sd);

		action();
	}

	@Override
	public void action() {
		try {
			DFAgentDescription[] result = DFService.search(myAgent, dfTemplate);
			if(result.length!=lastSize)
			{
				receivers=new AID[result.length];
				for(int i=0;i<result.length;++i)
				{
					receivers[i]=result[i].getName();
				}
				senderGui.refreshReceiverList(receivers);
				lastSize=result.length;
			}
			else
			{
				block();
			}
			
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}


