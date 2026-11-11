namespace CS_Study.Chapter06
{
    internal class SimpleRemoteControl
    {
        Command slot;

        public SimpleRemoteControl() { }

        public void SetCommand(Command command)
        {
            slot = command;
        }

        public void ButtonWasPressed()
        {
            slot.Execute();
        }
    }
}
