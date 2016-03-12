package me.winterguardian.chatmanager;

public class CommandRedirection
{
  private String command;
  private String redirection;
  private boolean canByPass;
  
  public CommandRedirection(String command, String redirection, boolean canByPass)
  {
    setCommand(command);
    setRedirection(redirection);
    setCanByPass(canByPass);
  }
  
  public String getCommand()
  {
    return command;
  }
  
  public void setCommand(String command)
  {
    this.command = command;
  }
  
  public String getRedirection()
  {
    return redirection;
  }
  
  public void setRedirection(String redirection)
  {
    this.redirection = redirection;
  }
  
  public boolean canByPass()
  {
    return canByPass;
  }
  
  public void setCanByPass(boolean canByPass)
  {
    this.canByPass = canByPass;
  }
}
