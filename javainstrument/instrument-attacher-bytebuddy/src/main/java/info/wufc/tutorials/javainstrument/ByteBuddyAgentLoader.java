package info.wufc.tutorials.javainstrument;

import java.io.File;

import net.bytebuddy.agent.ByteBuddyAgent;

public class ByteBuddyAgentLoader {

    public static void main(String[] args){
        String agentFilePath = "D:\\project\\self\\avp42\\tutorials\\javainstrument\\instrument-agent-bytebuddy\\target\\instrument-agent-bytebuddy.jar";

        File agentFile = new File(agentFilePath);
        try{
            System.out.println("Attaching to target jvm with pid:" + args[0]);
            ByteBuddyAgent.attach(agentFile.getAbsoluteFile(), args[0]);

            System.out.println("Attached to target jvm and loaded agent successfully");
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
