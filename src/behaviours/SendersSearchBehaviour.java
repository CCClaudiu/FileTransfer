package behaviours;

import guiPackage.ReceiverGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SendersSearchBehaviour extends CyclicBehaviour{
	private DFAgentDescription dfTemplate;
	private AID[] sendersArray;
	private ReceiverGUI clientGui;
	private int lastSize=-1;
	public SendersSearchBehaviour(Agent a,AID[] refSendersArray,ReceiverGUI refClientGui) {
		super(a);
		this.sendersArray=refSendersArray;
		this.clientGui=refClientGui;
		dfTemplate = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("sender");
		dfTemplate.addServices(sd);

		action();
	}

	@Override
	public void action() {
		try {
			DFAgentDescription[] result = DFService.search(myAgent, dfTemplate);
			if(result.length!=lastSize)
			{
				sendersArray = new AID[result.length];
				for (int i = 0; i < result.length; ++i) {
					sendersArray[i] = result[i].getName();
					// System.out.println(result[i].getName());
				}
				clientGui.refreshSendersList(sendersArray);
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