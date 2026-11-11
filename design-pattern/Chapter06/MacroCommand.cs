namespace CS_Study.Chapter06
{
    internal class MacroCommand : Command
    {
        Command[] commands;

        public MacroCommand( Command[] commands )
        {
            this.commands = commands;
        }
        
        public void Execute()
        {
            for (int i = 0; i < commands.Length; i++ )
            {
                commands[ i ].Execute();
            }
        }

        public void Undo()
        {
            for ( int i = 0; i < commands.Length; i++ )
            {
                commands[ i ].Undo();
            }
        }
    }
}
