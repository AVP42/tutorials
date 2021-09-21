package info.wufc.tutorials.javainstrument;

import java.io.File;
import java.util.Optional;

import com.sun.tools.attach.VirtualMachine;

public class Loader {

    public static void main(String[] args){
        String applicationName = "Application";
        String agentFilePath = "D:\\project\\self\\avp42\\tutorials\\javainstrument\\instrument-agent-bytebuddy\\target\\instrument-agent-bytebuddy.jar";
        Optional<String> jvmPidOpt = Optional.ofNullable(VirtualMachine.list().stream().filter(jvm -> {
            System.out.println("jvm: " + jvm.displayName() + " pid:" + jvm.id());
            return jvm.displayName().contains(applicationName);
        }).findFirst().get().id());

        if (!jvmPidOpt.isPresent()) {
            System.err.println("Target Application not found!");
            return ;
        }

        File agentFile = new File(agentFilePath);
        try{
            System.out.println("Attaching to target jvm with pid:" + jvmPidOpt.get());
            VirtualMachine jvm = VirtualMachine.attach(jvmPidOpt.get());
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();
            System.out.println("Attached to target jvm and loaded agent successfully");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
