namespace CS_Study.Chapter06
{
    internal interface Command
    {
        public void Execute();
        public void Undo();
    }
}
